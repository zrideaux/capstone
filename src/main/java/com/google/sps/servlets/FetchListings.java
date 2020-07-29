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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.sps.data.Listing;
import com.google.sps.utility.GetParameter;

/** 
 * Servlet that creates comment objects from entities and returns the list of 
 *    comment entities.
 */
@WebServlet("/fetch-listings")
public class ListCommentsServlet extends HttpServlet {

  static final int COMMENT_LIMIT = 30;

  /** 
   * Will only show the 30 most recent comments.
   * Returns the comments associated with the page the user is on, which 
   *    is found based on their input.
   *
   * @param request which contains data to retrieve listings
   * @param response listings in the form of json (List<Listings>)
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    // Receive input from the modify number of comments shown form
    String listingKeys = GetParameter.getParameter(request, "listing-keys", "");
    
    if (listingKeys.length() == 0) {
      // Return error no listings
      throw new Exception("There are no listing keys.");
    }

    String[] listingKeysArray = listingKeys.split(" ");
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // If there are comments then return a list of comments
    List<Listing> listings = new ArrayList<> ();

    for (String listingKey : listingKeysArray) {
      Entity listing = datastore.get(listingKey);
      listings.add(createListing(listing));
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
    String description = entity.getProperty("description");
    String howToHelp = entity.getProperty("howToHelp");
    String location = entity.getProperty("location");
    String name = entity.getProperty("name");
    long timestamp = entity.getProperty("timestamp");
    String type = entity.getProperty("type");
    int upvotes = entity.getProperty("upvotes");
    int downvotes = entity.getProperty("downvotes");
    int views = entity.getProperty("views");

    return new Listing(description, howToHelp, location, name, timestamp, type, 
        upvotes, downvotes, views);
  }
}
