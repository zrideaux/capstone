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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.sps.data.Listing;
import com.google.sps.utility.ValidateInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
      String[] listingEntityKeysStringArray = listingEntityKeysString.trim().split(",");
      
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

      // If there are listing keys then return a List of Listings
      try {
        listings = Listing.createListingArray(datastore, 
            listingEntityKeysStringArray);
      } catch (Exception e) {
        // Return a JSON errorMessage with the exception message
        ValidateInput.createErrorMessage(e, response);
        return;
      }
    }

    String jsonListings = new Gson().toJson(listings);
    response.setContentType("application/json;");
    response.getWriter().println(jsonListings);
  }

  
}
