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

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gson.Gson;
import com.google.sps.utility.ValidateInput;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Returns a URL that allows the user to upload a file to  
 *    Blobstore. 
 */
@WebServlet("/blobstore-upload-url")
public class BlobstoreUploadUrl extends HttpServlet {

  static final int SERVLET_URL_MAX_CHAR = 30;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    String servletUrl;
    try {
      servletUrl = ValidateInput.getUserString(request, "servlet-url", 1, 
          SERVLET_URL_MAX_CHAR);
    } catch (Exception e) {
      ValidateInput.createErrorMessage(e, response);
      return;
    }    
    System.err.println("Servlet url: " + servletUrl);
    String uploadUrl = blobstoreService.createUploadUrl(servletUrl); 
    System.err.println("Upload url: " + uploadUrl);

    response.setContentType("application/json;");
    String jsonUploadUrl = new Gson().toJson(uploadUrl);
    response.getWriter().println(jsonUploadUrl);
  }
}
