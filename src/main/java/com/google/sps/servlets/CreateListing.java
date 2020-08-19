// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
 
package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.User;
import com.google.sps.utility.AuthenticationUtility;
import com.google.sps.utility.ListingConstants;
import com.google.sps.utility.ValidateInput;

/** Servlet takes in information from form on newlisting.html and creates listing entity*/
@WebServlet("/create-listing")
public class CreateListing extends HttpServlet {
  
  /** Uses getParmeter function to obtain user input and inserts that input into  Entity for storage.*/
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();

    // Only allow user to make listing if logged in
    if (userService.isUserLoggedIn()) {
      // The following variables are required and have a max char limit
      String description;
      try {
        description = ValidateInput.getUserString(request, "description", 1,
            ListingConstants.MAX_CONTENT_LEN);
      } catch (Exception e) {
        ValidateInput.createErrorMessage(e, response);
        return;
      }

      String howToHelp;
      try {
        howToHelp = ValidateInput.getUserString(request, "howToHelp", 1,
            ListingConstants.MAX_CONTENT_LEN);
      } catch (Exception e) {
        ValidateInput.createErrorMessage(e, response);
        return;
      } 

      String location;
      try {
        location = ValidateInput.getUserString(request, "location", 1,
            ListingConstants.MAX_LOCATION_LEN);
      } catch (Exception e) {
        ValidateInput.createErrorMessage(e, response);
        return;
      }

      String name;
      try {
        name = ValidateInput.getUserString(request, "name", 1,
            ListingConstants.MAX_NAME_LEN);
      } catch (Exception e) {
        ValidateInput.createErrorMessage(e, response);
        return;
      }

      String tags;
      try {
        tags = ValidateInput.getUserString(request, "tags", 1,
            ListingConstants.MAX_TAGS_LEN).toLowerCase();
      } catch (Exception e) {
        ValidateInput.createErrorMessage(e, response);
        return;
      }

      String type;
      try {
        type = ValidateInput.getUserString(request, "type", 1,
            ListingConstants.MAX_TYPE_LEN);
      } catch (Exception e) {
        ValidateInput.createErrorMessage(e, response);
        return;
      }

      // Uploading an image is optional
      String imageURL = ValidateInput.getUploadedFileUrl(request, "image", "https://i.imgur.com/wE5wDVZ.png");

      // There are no char limit for website and website is optional
      String website = ValidateInput.getParameter(request, "website", "");
    
      long timestamp = System.currentTimeMillis();

      Entity listingEntity = new Entity("Listing");
      listingEntity.setProperty("description", description);
      listingEntity.setProperty("howToHelp", howToHelp);
      listingEntity.setProperty("imageURL", imageURL);
      listingEntity.setProperty("location", location);
      listingEntity.setProperty("name", name);
      listingEntity.setProperty("tags", tags);
      listingEntity.setProperty("timestamp",timestamp);
      listingEntity.setProperty("type", type);
      listingEntity.setProperty("upvotedUserKeys", " ");
      listingEntity.setProperty("upvotes", 0);
      listingEntity.setProperty("downvotes", 0);
      listingEntity.setProperty("views", 0);
      listingEntity.setProperty("website", website);

      // Place the new listing entity in datastore and save its key
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Key listingEntityKey = datastore.put(listingEntity);

      // Get current user and place the entity in their created listings property
      Entity currentUser = AuthenticationUtility.getCurrentUserEntity(
          datastore, userService);
      User.addListingKeyToUserEntity(datastore, currentUser,
          listingEntityKey, "createdListingKeys");

      // Returns a success message since everything went smoothly
      ValidateInput.createSuccessMessage(response);
    } else {
      ValidateInput.createErrorMessage("User is not logged in.", response);
    }
  }
}
