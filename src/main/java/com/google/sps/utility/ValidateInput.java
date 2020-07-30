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

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

// Holds functions used by servlets in order to get parameters
public final class ValidateInput {
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
  public static String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      System.out.println("Default");
      return defaultValue;
    }
    return value;
  }

  /** 
   * Creates an error message 
   * 
   * @param e the exception that was thrown
   * @param response a json error message 
   */
  public static void createErrorMessage(Exception e, 
      HttpServletResponse response) throws IOException {
    String errorMessage = "Servlet Error: " + e.getMessage();
    System.err.println(errorMessage);

    String jsonErrorMessage = new Gson().toJson(errorMessage);
    response.setContentType("application/json;");
    response.getWriter().println(jsonErrorMessage);
  }

  /** 
   * Creates an error message 
   * 
   * @param e the exception that was thrown
   * @param response a json error message 
   */
  public static void createErrorMessage(String error, 
      HttpServletResponse response) throws IOException {
    String errorMessage = "Servlet Error: " + error;
    System.err.println(errorMessage);

    String jsonErrorMessage = new Gson().toJson(errorMessage);
    response.setContentType("application/json;");
    response.getWriter().println(jsonErrorMessage);
  }
}