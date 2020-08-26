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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.sps.data.User;
import com.google.sps.utility.ValidateInput;
import java.lang.Math;
import java.lang.Long;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

@WebServlet("/delete-listing")
public class DeleteListing extends HttpServlet {
  /**
   * Deletes a Listing, and removes its key String from any user's
   *     upvotedListingKey property that upvoted this listing.
   *
   * @param request contains data to delete a listing.
   * @param response a success message if the listing was deleted or an error
   *     message if the listing was not.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // Initialize parameters
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    String listingKeyString = ValidateInput.getParameter(request, "listing-key",
        "");
    Key listingKey = KeyFactory.stringToKey(listingKeyString);

    Entity listingEntity;
    try { 
      listingEntity = datastore.get(listingKey);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    }

    // The user limit is the number of upvotes the listing has.
    int userLimit = Math.toIntExact((long) listingEntity.getProperty("upvotes"));

    // Get all users 
    Query queryUser = new Query("User");
    PreparedQuery preparedQueryListings = datastore.prepare(queryUser);
    Iterable<Entity> userEntities = preparedQueryListings.asIterable();

    // Go through all of the users and remove the upvoted listing key string
    for (Entity userEntity : userEntities) {
      String property = "upvotedListingKeys";
      String upvotedListingKeys = (String) userEntity.getProperty(
          property);

      if (upvotedListingKeys.contains(listingKeyString)) {
        upvotedListingKeys = upvotedListingKeys.replaceFirst(
            listingKeyString + " ", "");
        userEntity.setProperty(property, upvotedListingKeys);
        datastore.put(userEntity);
      }
    }

    datastore.delete(listingKey);

    ValidateInput.createSuccessMessage(response);
  }
}
