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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.sps.data.User;
import com.google.sps.utility.AuthenticationUtility;
import com.google.sps.utility.ValidateInput;

/** 
 * Servlet that creates a User object from email and returns the User object.
 */
@WebServlet("/fetch-user")
public class FetchUser extends HttpServlet {
  /** 
   * Returns JSON which is a User object or an 
   *     error message if an exception is caught.
   *
   * @param request which contains data to retrieve user Entity
   * @param response User in the form of json or an error 
   *     message in the form of JSON
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    // DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // // Get the user Entity object from the 
    // Entity userEntity;
    // try {
    //   userEntity = AuthenticationUtility.getCurrentUserEntity(datastore, 
    //       request);
    // } catch (Exception e) {
    //   ValidateInput.createErrorMessage(e, response);
    //   return;
    // }

    // User user = User.createUser(userEntity);

    // String jsonUser = new Gson().toJson(user);
    // response.setContentType("application/json;");
    // response.getWriter().println(jsonUser);
  }
}
