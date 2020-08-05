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


public class AuthenticationUtility {
  /**
   * Return a User Entity that matches a provided email.
   *
   * @param email the email to check for in the query.
   * @return a User Entity matching email
   */
  public static Entity getUserByEmail(DatastoreService datastore, String email) {
    // Perform a query to find the account associated with email
    Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
    Query userQuery = new Query("User").setFilter(emailFilter);
    PreparedQuery preparedQuery = datastore.prepare(userQuery);
    Entity user = preparedQuery.asSingleEntity();

    return user;
  }

  /**
   * Return a boolean detailing if there is an account associated with a given
   * email.
   * 
   * @param email the email to check for in the query.
   * @return true if account associated with email exists
   */
  public static boolean userAlreadyHasAccount(DatastoreService datastore, String email) {
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

  public static Entity getCurrentUserEntity(DatastoreService datastore, UserService userService) {
    String userEmail = userService.getCurrentUser().getEmail();
    Entity userEntity = getUserByEmail(datastore, userEmail);
    return userEntity;
  }
}
