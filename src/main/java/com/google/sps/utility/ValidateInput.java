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
  public static String getParameter(HttpServletRequest request, String name, 
      String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      System.out.println("Default");
      return defaultValue;
    }

    return value;
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
  public static int getParameter(HttpServletRequest request, String name, 
      int defaultValue) throws Exception {
    String value = request.getParameter(name);
    if (value == null) {
      System.out.println("Default");
      return defaultValue;
    }

    int valueNum;
    try {
      valueNum = Integer.parseInt(value);
    } catch (NumberFormatException e) {
      String error = "Could not convert to int: " + value;
      throw new Exception(error);
    }    

    return valueNum;
  }  

  /** 
   * Returns the String input entered by the user, or throws an exception if
   * the input was invalid.
   * Min must be greater than -1 and Max must be greater 
   * than or equal to min or else an exception is thrown.
   *
   * @param request the request received from the form that contains user input
   * @param parameter the name of the input parameter one is retreiving
   * @param min used to establish the lower bound of the input
   * @param max used to establish the upper bound of the input
   * @return the user's input (number) or -1 if it does not follow guidelines
   */
  static String getUserInput(HttpServletRequest request, String parameter, 
      int min, int max) throws Exception {

    if (max < min) {
      String error = "Max (" + max + ") must be greater than or equal to" + 
          " Min (" + min + ")";
      throw new Exception(error);
    }

    // Get the input from the form.
    String userInputString = request.getParameter(parameter);
    if (userInputString == null) {
      String error = "Paramter " + parameter + " was not found";
      throw new Exception(error);
    }

    return userInputString;
  }

  /**
   * Check that the value is between min and max. If it's not throw an 
   * Exception.
   *
   * @param value an int that is being compared to bounds
   * @param min a lower bound int
   * @param max an upper bound int
   */

  static void isInputInBounds(int value, String parameter, int min, int max) 
      throws Exception {
    if (value < min || value > max) {
      String error = "Value for " + parameter + " is out of range (" + min 
          + " - " + max + "): " + value;
      throw new Exception(error);
    }    
  }

  /** 
   * Returns the int input entered by the user, or throws an exception if
   * the input was invalid or not between the bounds.
   * Max must be greater than or equal to min or else an exception is thrown.
   *
   * @param request the request received from the form that contains user input
   * @param parameter the name of the input parameter one is retreiving
   * @param min used to establish the lower bound of the input
   * @param max used to establish the upper bound of the input
   * @return the user's input (int) or throw an exception if it does not 
   * follow guidelines
   */
  public static int getUserNum(HttpServletRequest request, String parameter, 
      int min, int max) throws Exception {
    // Get the input from the form.
    String userInputString;
    try {
      userInputString = getUserInput(request, parameter, min, max);
    } catch (Exception e) {
      throw e;
    }

    // Convert the input to an int.
    int userNum;
    try {
      userNum = Integer.parseInt(userInputString);
    } catch (NumberFormatException e) {
      String error = "Could not convert to int: " + userInputString;
      throw new Exception(error);
    }

    // Check that the input is between min and max.
    try {
      isInputInBounds(userNum, parameter, min, max);
    } catch (Exception e) {
      throw e; 
    }

    return userNum;
  }

  /** 
   * Returns the int input entered by the user, throws an exception if
   *     the input was invalid or not between the bounds, or returns the 
   *     default value if the value for the parameter is null.
   * Max must be greater than or equal to min or else an exception is thrown.
   *
   * @param request the request received from the form that contains user input
   * @param parameter the name of the input parameter one is retreiving
   * @param min used to establish the lower bound of the input
   * @param max used to establish the upper bound of the input
   * @param defaultValue return this value if the value for a parameter is null
   * @return the user's input (int) or throw an exception if it does not 
   * follow guidelines
   */
  public static int getUserNum(HttpServletRequest request, String parameter, 
      int min, int max, int defaultValue) throws Exception {
    // Get the input from the form.
    String userInputString;
    try {
      userInputString = getUserInput(request, parameter, min, max);
    } catch (Exception e) {
      return defaultValue;
    }

    // Convert the input to an int.
    int userNum;
    try {
      userNum = Integer.parseInt(userInputString);
    } catch (NumberFormatException e) {
      String error = "Could not convert to int: " + userInputString;
      throw new Exception(error);
    }

    // Check that the input is between min and max.
    try {
      isInputInBounds(userNum, parameter, min, max);
    } catch (Exception e) {
      throw e; 
    }

    return userNum;
  }  

  /** 
   * Returns the String input entered by the user, or throws an exception if
   * the input was invalid or not between the bounds.
   * Min must be greater than -1 and Max must be greater 
   * than or equal to min or else an exception is thrown.
   *
   * @param request the request received from the form that contains user input
   * @param parameter the name of the input parameter one is retreiving
   * @param min used to establish the lower bound of the input
   * @param max used to establish the upper bound of the input
   * @return the user's input (String) or throw and exception if it does not 
   * follow guidelines
   */
  public static String getUserString(HttpServletRequest request, 
      String parameter, int min, int max) throws Exception {
    // The length of a string cannot be less than 0
    if (min <= -1) {
      String error = "Min (" + min + ") must be greater than -1 ";
      throw new Exception(error);
    }

    // Get the input from the form.
    String userInputString;
    try {
      userInputString = getUserInput(request, parameter, min, max);
    } catch (Exception e) {
      throw e;
    }

    // Check that the input is between min and max.
    try {
      isInputInBounds(userInputString.length(), parameter, min, max);
    } catch (Exception e) {
      throw e; 
    }

    return userInputString;
  }

  /** 
   * Returns the String input entered by the user, throws an exception if
   *     the input was invalid or not between the bounds, or return the default 
   *     value if the value for a parameter is null.
   * Min must be greater than -1 and Max must be greater 
   * than or equal to min or else an exception is thrown.
   *
   * @param request the request received from the form that contains user input
   * @param parameter the name of the input parameter one is retreiving
   * @param min used to establish the lower bound of the input
   * @param max used to establish the upper bound of the input
   * @param defaultValue return this value if the value for a parameter is null
   * @return the user's input (String) or throw and exception if it does not 
   * follow guidelines
   */
  public static String getUserString(HttpServletRequest request, 
      String parameter, int min, int max, String defaultValue) throws Exception {
    // The length of a string cannot be less than 0
    if (min <= -1) {
      String error = "Min (" + min + ") must be greater than -1 ";
      throw new Exception(error);
    }

    // Get the input from the form.
    String userInputString;
    try {
      userInputString = getUserInput(request, parameter, min, max);
    } catch (Exception e) {
      return defaultValue;
    }

    // Check that the input is between min and max.
    try {
      isInputInBounds(userInputString.length(), parameter, min, max);
    } catch (Exception e) {
      throw e; 
    }

    return userInputString;
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
}
