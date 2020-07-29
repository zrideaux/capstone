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

import java.util.List;
import com.google.gson.Gson;
import java.sql.Timestamp;
import java.util.Date;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet takes in information from form on newlisting.html and creates listing entity*/
@WebServlet("/create-listing")
public class CreateListing extends HttpServlet {
  
  /** Uses getParmeter function to obtain user input and inserts that input into  Entity for storage.*/
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = getParameter(request, "cause-name", "");
    String type = getParameter(request, "type", "");
    String location = getParameter(request, "location", "");
    String howToHelp = getParameter(request, "cause-how-to-help", "");    
    String description = getParameter(request, "cause-description", "");
    String website = getParameter(request, "cause-website", "");
    long timestamp = System.currentTimeMillis();
    int upvotes = 0;
    int downVotes = 0;
    int views = 0; 

    Entity listingEntity = new Entity("Listing");
    listingEntity.setProperty("name", name);
    listingEntity.setProperty("type", type);
    listingEntity.setProperty("location", location);
    listingEntity.setProperty("howToHelp", howToHelp);
    listingEntity.setProperty("description", description);
    listingEntity.setProperty("upvotes", 0);
    listingEntity.setProperty("downvotes", 0);
    listingEntity.setProperty("views", 0);
    listingEntity.setProperty("timestamp",timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();    
    
    datastore.put(listingEntity);
    
    response.sendRedirect("/index.html");
  }
  /**
   * Obtains user input and returns the value of that input.
   * 
   * @returns The value of the input, or defaultValue if name does not exist.
   */
  private String getParameter(HttpServletRequest request, String attribute, String defaultValue) {
    String value = request.getParameter(attribute);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
