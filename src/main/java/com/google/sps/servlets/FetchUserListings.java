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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.sps.data.Listing;
import com.google.sps.utility.ValidateInput;

/** 
 * Servlet that creates comment objects from entities and returns the list of 
 *    comment entities.
 */
@WebServlet("/fetch-user-listings")
public class FetchUserListings extends HttpServlet {

  static final int COMMENT_LIMIT = 30;

  /** 
   * Returns JSON which is a List of Listings associated with the user or an 
   *     error message if an exception is caught.
   *
   * @param request which contains data to retrieve listings
   * @param response listings in the form of json (List<Listings>) or an error 
   *     message in the form of JSON
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    // Receive input from the modify number of comments shown form
    String listingKeysStr = ValidateInput.getParameter(request, "listing-keys",
        "");

    // A Listings of listings to return
    List<Listing> listings = new ArrayList<> ();

    // Add Listings to the List if there are keys
    if (listingKeysStr.length() > 0) {
      String[] listingKeysStrArray = listingKeysStr.split(" ");
      
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

      // If there are comments then return a list of comments
      for (String listingKeyStr : listingKeysStrArray) {
        Key listingKey = KeyFactory.stringToKey(listingKeyStr);
        Entity listing;
        try {
          listing = datastore.get(listingKey);
        } catch (Exception e) {
          // Return a JSON errorMessage with the exception message
          ValidateInput.createErrorMessage(e, response);
          return;
        }
        listings.add(createListing(listing));
      } 
    }

    String jsonListings = new Gson().toJson(listings);
    response.setContentType("application/json;");
    response.getWriter().println(jsonListings);
  }

  /**
   * Creates a Listing object from an Entity object that represents a listing
   *
   * @param entity the entity that represents a listing
   * @return a Listing with all of the properties from the Entity
   */
  public static Listing createListing(Entity entity) {
    String description = (String) entity.getProperty("description");
    String howToHelp = (String) entity.getProperty("howToHelp");
    String location = (String) entity.getProperty("location");
    String name = (String) entity.getProperty("name");
    long timestamp = (long) entity.getProperty("timestamp");
    String type = (String) entity.getProperty("type");
    int upvotes = (int) entity.getProperty("upvotes");
    int downvotes = (int) entity.getProperty("downvotes");
    int views = (int) entity.getProperty("views");

    return new Listing(description, howToHelp, location, name, timestamp, type, 
        upvotes, downvotes, views);
  }
}
