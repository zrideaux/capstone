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

package com.google.sps.utility;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
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
import java.util.Collections;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Utility extends HttpServlet { 
  
  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

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
  public String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      System.out.println("Default");
      return defaultValue;
    }
    return value;
  }

  /**
   * Return a User Entity that matches a provided email.
   * 
   * @param email the email to check for in the query.
   * @return a User Entity matching email
   */
  public Entity getUserByEmail(String email) {
    // Perform a query to find the account associated with email
    Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
    Query userQuery = new Query("User").setFilter(emailFilter);
    PreparedQuery preparedQuery = datastore.prepare(userQuery);
    Entity user = preparedQuery.asSingleEntity();
    
    return user;
  }

  /**
   * Verifies and returns an id token from the "idtoken" parameter
   *
   * @param request an http request to the servlet
   * @return a verified Google id token or null if it is invalid
   */
  public GoogleIdToken getIdToken(HttpServletRequest request) {
    try {
      String CLIENT_ID = "CLIENT_ID_GOES_HERE";

      HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
      JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

      // Get token to be verified
      String idTokenString = request.getParameter("idtoken");

      // Check if the token is valid, set idToken to null if it isn't 
      GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
          .setAudience(Collections.singletonList(CLIENT_ID))
          .build();
      GoogleIdToken idToken = verifier.verify(idTokenString);

      return idToken;
    } catch (Exception e) {
      return null;
    }
  }
  
  /**
   * Return a boolean detailing if there is an account associated with a given
   * email.
   * 
   * @param email the email to check for in the query.
   * @return true if account associated with email exists
   */
  public boolean userAlreadyHasAccount(String email) {
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
}