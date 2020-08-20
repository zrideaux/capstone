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
import com.google.sps.utility.ValidateInput;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet ("/verify-listing-ownership")
public class VerifyListingOwnership extends HttpServlet {
  /** 
   * 
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
          ValidateInput.createSuccessMessage(response);
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
}
