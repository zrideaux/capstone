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
 * Servlet that creates Listings from Entities and returns the list of 
 *    Listings.
 */
@WebServlet("/fetch-user-listings")
public class FetchUserListings extends HttpServlet {
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
    String listingEntityKeysString = ValidateInput.getParameter(request, 
        "listing-keys", "");

    // A Listings of listings to return
    List<Listing> listings = new ArrayList<Listing>();

    // Add Listings to the List if there are keys
    if (listingEntityKeysString.length() > 0) {
      String[] listingEntityKeysStrArray = listingEntityKeysString.split(" ");
      
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

      // If there are listing keys then return a List of Listings
      for (String listingEntityKeyStr : listingEntityKeysStrArray) {
        Key listingEntityKey = KeyFactory.stringToKey(listingEntityKeyStr);
        Entity listingEntity;
        try {
          listingEntity = datastore.get(listingEntityKey);
        } catch (Exception e) {
          // Return a JSON errorMessage with the exception message
          ValidateInput.createErrorMessage(e, response);
          return;
        }
        listings.add(createListing(listingEntity));
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
    String website = (String) entity.getProperty("website");

    return new Listing(description, howToHelp, location, name, timestamp, type, 
        upvotes, downvotes, views, website);
  }
}
