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
import com.google.sps.data.Listing;
import com.google.sps.utility.ValidateInput;

/** 
 * Servlet that creates a Listing from a form and returns it in a list of 
 *    Listings.
 */
@WebServlet("/create-listing-preview")
public class CreateListingPreview extends HttpServlet {

  static final int MAX_CONTENT_LEN = 256;
  static final int MAX_LOCATION_LEN = 256;
  static final int MAX_NAME_LEN = 50; 
  // Based on the length of the longest category name (fundraiser)
  static final int MAX_TYPE_LEN = 10; 
  
  /** 
   * Returns JSON which is a List of Listings associated with the user or an 
   *     error message if an exception is caught.
   *
   * @param request which contains data to create Listing object
   * @param response listings in the form of json (List<Listings>) or an error 
   *     message in the form of JSON
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    
    // The following variables are required and have a max char limit
    String description;
    try {
      description = ValidateInput.getUserString(request, "description", 1, 
          MAX_CONTENT_LEN);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    }  

    String howToHelp;
    try {
      howToHelp = ValidateInput.getUserString(request, "howToHelp", 1, 
          MAX_CONTENT_LEN);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    }  

    String location;
    try {
      location = ValidateInput.getUserString(request, "location", 1, 
          MAX_LOCATION_LEN);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    String name;
    try {
      name = ValidateInput.getUserString(request, "name", 1, 
          MAX_NAME_LEN);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    String type;
    try {
      type = ValidateInput.getUserString(request, "type", 1, 
          MAX_TYPE_LEN);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    } 

    // Uploading an image is optional
    // String imageURL = ValidateInput.getUploadedFileUrl(request, "image"); 
    String imageURL; 
    try {
      imageURL = ValidateInput.getUploadedFileUrl(request, "image");
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    }

    // There are no char limit for website and website is optional
    String website = ValidateInput.getParameter(request, "website", "");

    int upvotes = 0;
    int downvotes = 0;
    int views = 0;  
    long timestamp = System.currentTimeMillis();

    Listing listing = new Listing(description, howToHelp, imageURL, location, 
        name, timestamp, type, upvotes, downvotes, views, website);

    String jsonListing = new Gson().toJson(listing);
    response.setContentType("application/json;");
    response.getWriter().println(jsonListing);
  }
}
