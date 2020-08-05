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
import com.google.sps.data.User;
import com.google.sps.utility.AuthenticationUtility;
import com.google.sps.utility.ValidateInput;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/authentication")
public class Authentication extends HttpServlet {
  /**
   * Checks if a user is logged in and performs appropriate actions
   * depending whether they are or not.
   *
   * @param request an http request to the servlet
   * @param response the http response sent 
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    HashMap<String, String> authenticationInfo = new HashMap<String, String>();
    final String USER_IS_LOGGED_IN_KEY = "userIsLoggedIn";
    final String USER_IS_ADMIN_KEY = "userIsAdmin";
    final String USER_EMAIL_KEY = "userEmail";
    final String USER_ENTITY_STATUS_KEY = "userEntityStatus";
    final String LOGOUT_LINK_KEY = "logoutLink";
    final String LOGIN_LINK_KEY = "loginLink";
    Gson gson = new Gson();

    // Check whether or not current user is logged in.
    if (userService.isUserLoggedIn()) {
      // Create information to return to front end.
      String userIsLoggedIn = "true";
      String userIsAdmin = String.valueOf(userService.isUserAdmin());
      String userEmail = userService.getCurrentUser().getEmail();
      String userEntityStatus;
      String logoutLink = userService.createLogoutURL("/");

      // Check if an existing user entity is associated with the current user's
      // email. If not, create a new user entity that is.
      if (AuthenticationUtility.userAlreadyHasAccount(datastore, userEmail)) {
        userEntityStatus = "No user entity created."
            + " There is already a user associated with this email.";
      } else {
        datastore.put(User.createUserEntity(userEmail));
        userEntityStatus = "New user entity created.";
      }

      // Add information to be returned to a hashmap.
      authenticationInfo.put(USER_IS_LOGGED_IN_KEY, userIsLoggedIn);
      authenticationInfo.put(USER_IS_ADMIN_KEY, userIsAdmin);
      authenticationInfo.put(USER_EMAIL_KEY, userEmail);
      authenticationInfo.put(USER_ENTITY_STATUS_KEY, userEntityStatus);
      authenticationInfo.put(LOGOUT_LINK_KEY, logoutLink);
    } else {
      // Create information to return to front end.
      String userIsLoggedIn = "false";
      String userIsAdmin = "false";
      String loginLink = userService.createLoginURL("/");
      
      // Add information to be returned to a hashmap.
      authenticationInfo.put(USER_IS_LOGGED_IN_KEY, userIsLoggedIn);
      authenticationInfo.put(USER_IS_ADMIN_KEY, userIsAdmin);
      authenticationInfo.put(LOGIN_LINK_KEY, loginLink);
    }
  
    // Convert the authenticationInfo hashmap into JSON.
    String authenticationInfoJson = gson.toJson(authenticationInfo);
    response.setContentType("application/json");
    System.out.println("AUTHENTICATION INFO: " + authenticationInfoJson);
    response.getWriter().println(authenticationInfoJson);
  }
}
