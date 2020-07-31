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

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.sps.utility.Utility;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/create-user")
public class CreateUser extends HttpServlet {

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  /**
   * Checks if a user has the ability to create a new user entity and creates
   * and adds one to the database if they do.
   * 
   * @param request an http request to the servlet
   * @param response the http response sent 
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Utility utility = new Utility();
    String message = new String();

    // Verify that user is signed in with a valid account
    GoogleIdToken idToken = utility.getIdToken(request);

    if (idToken != null) {
      // Get payload for user
      Payload user = idToken.getPayload();

      // Get user email
      String userEmail = user.getEmail();
      System.out.println(utility.getUserByEmail(userEmail));

      // Check if user's email is already associated with an account.        
      if (utility.userAlreadyHasAccount(userEmail)) {
        message = "No user created. There is already a user associated with this email.";
      } else {
        createUgiserEntity(request, userEmail);
        message = "New user created.";
      }
    } else {
      System.out.println("Invalid ID token.");
    }
  
    response.setContentType("text/plain");
    System.out.println(message);
    response.getWriter().println(message);
  }

  /**
   * Create a new user entity and place it in the datastore.
   * 
   * @param request an http request to the servlet
   * @param userEmail the email to be associated with a the new user entity
   */
  private void createUserEntity(HttpServletRequest request, String userEmail) {
    // Create User entity and add to datastore
    Entity newUserEntity = new Entity("User");
    newUserEntity.setProperty("email", userEmail);
    newUserEntity.setProperty("username", "");
    newUserEntity.setProperty("bio", "");
    newUserEntity.setProperty("createdListingKeys", "");
    newUserEntity.setProperty("upvotedListingKeys", "");

    datastore.put(newUserEntity);
  }
}
