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
import java.lang.Math;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.data.Comment;
import com.google.sps.utility.CommentConstants;
import com.google.sps.utility.ValidateInput;

/** 
 * Servlet that creates comment objects from entities and returns the list of 
 *    comment entities.
 */
@WebServlet("/fetch-listings")
public class ListCommentsServlet extends HttpServlet {

  static final int COMMENT_LIMIT = 30;

  /** 
   * Will only show the 30 most recent comments.
   * Returns the comments associated with the page the user is on, which 
   *    is found based on their input.
   *
   * @param request which contains data to retrieve listings
   * @param response listings in the form of json (List<Listings>)
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    // Receive input from the modify number of comments shown form
    String listingKeys;
    try {
      numComments = ValidateInput.getUserNum(request, "num-comments", 1, 
          CommentConstants.MAX_NUM_COMMENTS);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    }  

    String[] listingKeysArray = listingKeys.split(" ");
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // If there are comments then return a list of comments
    List<Listing> listings = new ArrayList<> ();

    for (String listingKey : listingKeysArray) {

    }
   

    String jsonComments = new Gson().toJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(jsonComments);
  }
}
