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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Listing;
import com.google.sps.utility.UpdateListingUtility;
import com.google.sps.utility.ValidateInput;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet ("/fetch-listing")
public class FetchListing extends HttpServlet {
  /** 
   * Creates and returns a Listing from a listing Key.
   *
   * @param request which contains data to retrieve user Entity
   * @param response User in the form of json or an error 
   *     message in the form of JSON
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    String listingKeyString = ValidateInput.getParameter(request, "listing-key",
        "");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    UserService userService = UserServiceFactory.getUserService();

    Entity listingEntity;
    try {
      listingEntity = UpdateListingUtility.getListingEntityIfUserOwnsIt(
          datastore, listingKeyString, userService);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    }

    Listing listing = Listing.createListing(listingEntity);

    String jsonListing = new Gson().toJson(listing);
    response.setContentType("application/json;");
    response.getWriter().println(jsonListing);
  }
}
