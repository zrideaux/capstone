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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Listing;
import com.google.sps.utility.EntityUtility;
import com.google.sps.utility.ListingConstants;
import com.google.sps.utility.ValidateInput;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet ("/update-listing-preview")
public class UpdateListingPreview extends HttpServlet {
  /** 
   * Creates an updated Listing preview.
   *
   * @param request contains data to update a listing.
   * @param response JSON that represents an array of listings (the current and 
   *     updated listings).
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    System.err.println("WE MADE IT");
    String listingKeyString = ValidateInput.getParameter(request, "listing-key",
        "");
    
    if (listingKeyString.length() > 0) {
      UserService userService = UserServiceFactory.getUserService();

      if (userService.isUserLoggedIn()) {
        String userEmail = userService.getCurrentUser().getEmail();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity listingEntity;
        try {
          listingEntity = datastore.get(KeyFactory.stringToKey(
              listingKeyString));
        } catch (Exception e) {
          ValidateInput.createErrorMessage(e, response);
          return;
        }

        String listingEntityOwnersEmail = (String) listingEntity.getProperty(
            "ownersEmail");

        if (userEmail.equals(listingEntityOwnersEmail)) {
          Listing[] listings = new Listing[2];
          listings[0] = Listing.createListing(listingEntity);

          // Update entity
          try {
            updateListingEntity(datastore, listingEntity, request);
          } catch (Exception e) {
            ValidateInput.createErrorMessage(e, response);
            return;
          }
          listings[1] = Listing.createListing(listingEntity);

          String jsonListings = new Gson().toJson(listings);
          response.setContentType("application/json;");
          response.getWriter().println(jsonListings);  
        } else {
          ValidateInput.createErrorMessage("User does not own this listing", 
              response);
        }
      } else {
        ValidateInput.createErrorMessage("User needs to login", response);
      }
    } else {
      ValidateInput.createErrorMessage("Missing listing key", response);
    }
  }

  /**
   * Updates a listing entity.
   *
   * @param datastore
   * @param entity a listing Entity to update.
   * @param request contains data to retrieve params.
   */
  public void updateListingEntity(DatastoreService datastore, Entity entity, 
      HttpServletRequest request) throws Exception {
    String description = ValidateInput.getUserString(request, "description",
        0, ListingConstants.MAX_CONTENT_LEN);

    String howToHelp = ValidateInput.getUserString(request, "howToHelp", 0,
        ListingConstants.MAX_CONTENT_LEN);

    String imageURL = ValidateInput.getUploadedFileUrl(request, "image", "");

    String location = ValidateInput.getUserString(request, "location", 0,
        ListingConstants.MAX_LOCATION_LEN);

    String name = ValidateInput.getUserString(request, "name", 0,
        ListingConstants.MAX_NAME_LEN);

    String type = ValidateInput.getUserString(request, "type", 0,
        ListingConstants.MAX_TYPE_LEN);

    String website = ValidateInput.getParameter(request, "website", "");

    /**
     * If length of the property value is 0 and the property value is the
     *     same as the original value, then keep the original property value.
     */ 
    EntityUtility.updateStringProperty(entity, "description", description);
    EntityUtility.updateStringProperty(entity, "howToHelp", howToHelp);
    EntityUtility.updateStringProperty(entity, "imageURL", imageURL);
    EntityUtility.updateStringProperty(entity, "location", location);
    EntityUtility.updateStringProperty(entity, "name", name);
    EntityUtility.updateStringProperty(entity, "type", type);
    
    entity.setProperty("website", website);
  }
}
