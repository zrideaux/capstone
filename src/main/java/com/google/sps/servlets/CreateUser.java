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
    String message = new String();
    UserService userService = UserServiceFactory.getUserService();

    // Verify that current user is logged in.
    if (userService.isUserLoggedIn()) {
      // Check if user's email is already associated with an account.
      String userEmail = userService.getCurrentUser().getEmail();
      
      if (userAlreadyHasAccount(userEmail)) {
        message = "No user created. There is already a user associated with this email.";
      } else {
        createUserEntity(request, userEmail);
        message = "New user created.";
      }
    } else {
      message = "No user created. User is not signed in.";
    }
    
    response.setContentType("text/plain");
    System.out.println(message);
    response.getWriter().println(message);
  }
  
  /**
   * Return a boolean detailing if there is an account associated with a given
   * email.
   * 
   * @param email the email to check for in the query.
   * @return true if account associated with email exists
   */
  private boolean userAlreadyHasAccount(String email) {
    // Perform a query to find any accounts associated with email
    Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
    Query userQuery = new Query("User").setFilter(emailFilter);
    PreparedQuery preparedQuery = datastore.prepare(userQuery);
    // If the query is empty, there isn't an account associated with email
    if (preparedQuery.countEntities(FetchOptions.Builder.withLimit(1)) == 0) {
      return false;
    } else {
      return true;
    }
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

  /**
  * Retrieve and return a parameter from the URL, or a default if
  * no parameter is provided.
  * 
  * @param request an http request to the servlet
  * @param name the name of the parameter to retrieve
  * @param defaultValue the value to return if a value for the parameter
  *     is not provided
  * @return the request parameter, or the default value if the parameter
  *     was not specified by the client
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
