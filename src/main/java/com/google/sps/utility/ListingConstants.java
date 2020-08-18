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

/** A class that contains constants for comment servlets */
public final class ListingConstants {
  public static final int MAX_CONTENT_LEN = 256;
  public static final int MAX_LOCATION_LEN = 256;
  public static final int MAX_NAME_LEN = 50;
  public static final int MAX_TAGS_LEN = 256;
  // Based on the length of the longest category name (fundraiser)
  public static final int MAX_TYPE_LEN = 10; 

  // Based on the length of the String when no filters are checked (MIN) or 
  //     when all filters are checked and separated by "@" (MAX)
  public static final int FILTER_MIN = 0;
  public static final int FILTER_MAX = 7;
  // Based on the length of the shortest/longest radius category 
  public static final int RADIUS_MIN = 2;
  public static final int RADIUS_MAX = 4;
  // Based on the number of sort categories
  public static final int SORT_MIN = 1;
  public static final int SORT_MAX = 3;

  public static final int LISTING_LIMIT = 50;
} 
