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
import com.google.appengine.api.datastore.FetchOptions.Builder;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/create-user")
public class CreateUser extends HttpServlet {

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String message = new String();
    UserService userService = UserServiceFactory.getUserService();

    // Verify that current user is logged in.
    if (userService.isUserLoggedIn()) {
      // Check if user's email is already associated with an account.
      String userEmail = userService.getCurrentUser().getEmail();
      Filter filter = new FilterPredicate("email", FilterOperator.EQUAL, userEmail);
      Query query = new Query("User").setFilter(filter);
      PreparedQuery results = datastore.prepare(query);

      if (results.countEntities(FetchOptions.Builder.withLimit(1)) == 0) {
        createUserEntity(request, userEmail);
        message = "New user created.";
      } else {
        message = "No user created. There is already a user associated with this email.";
      }
    } else {
      message = "No user created. User is not signed in.";
    }
    
    response.setContentType("text/plain");
    response.getWriter().println(message);
  }

  private void createUserEntity(HttpServletRequest request, String userEmail) {
    // Get remaining fields 
    String userName = getParameter(request, "name", "");
    String userBio = getParameter(request, "bio", "");

    // Create User entity and add to datastore
    Entity newUserEntity = new Entity("User");
    newUserEntity.setProperty("email", userEmail);
    newUserEntity.setProperty("username", userName);
    newUserEntity.setProperty("bio", userBio);
    newUserEntity.setProperty("createdListingKeys", "");
    newUserEntity.setProperty("upvotedListingKeys", "");

    datastore.put(newUserEntity);
  }

  /**
  * @return the request parameter, or the default value if the parameter
  *         was not specified by the client
  */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      System.out.println("Default");
      return defaultValue;
    }
    return value;
  }
}
