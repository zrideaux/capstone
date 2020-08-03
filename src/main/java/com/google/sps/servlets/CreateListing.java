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
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet takes in information from form on newlisting.html and creates listing entity*/
@WebServlet("/create-listing")
public class CreateListing extends HttpServlet {
  
  /** Uses getParmeter function to obtain user input and inserts that input into  Entity for storage.*/
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    String type;
    try {
      type = ValidateInput.getUserString(request, "type", 1, 
          ListingConstants.MAX_TYPE_LEN);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    // Uploading an image is optional
    String imageURL = ValidateInput.getUploadedFileUrl(request, "image", ""); 

    // There are no char limit for website and website is optional
    String website = ValidateInput.getParameter(request, "website", "");

    int upvotes = 0;
    int downvotes = 0;
    int views = 0;  
    long timestamp = System.currentTimeMillis();

    Entity listingEntity = new Entity("Listing");
    listingEntity.setProperty("name", name);
    listingEntity.setProperty("type", type);
    listingEntity.setProperty("location", location);
    listingEntity.setProperty("howToHelp", howToHelp);
    listingEntity.setProperty("description", description);
    listingEntity.setProperty("upvotes", upvotes);
    listingEntity.setProperty("downvotes", downvotes);
    listingEntity.setProperty("views", views);
    listingEntity.setProperty("timestamp",timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();    
    
    datastore.put(listingEntity);
    
    response.sendRedirect("/index.html");
  }
  /**
   * Obtains user input and returns the value of that input.
   * 
   * @returns The value of the input, or defaultValue if name does not exist.
   */
  private String getParameter(HttpServletRequest request, String attribute, String defaultValue) {
    String value = request.getParameter(attribute);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
