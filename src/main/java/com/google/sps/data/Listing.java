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

package com.google.sps.data;

import java.sql.Timestamp;
import java.util.Date;

/** A listing */ 
public final class Listing {

  private final String description;
  private final String howToHelp;
  private final String location;
  private final String name;
  private final String dateCreated;
  private final String type;
  private final int upvotes;
  private final int downvotes;
  private final int views;
  private final String website;

  public Listing(String description, String howToHelp, String location, 
      String name, long timestamp, String type, int upvotes, int downvotes, 
      int views, String website) {
    this.description = description;
    this.howToHelp = howToHelp;
    this.location = location;
    this.name = name;

    // turn int of timestamp into Date object
    String dateCreated = timestampToDate(timestamp);
    this.dateCreated = dateCreated;
    this.type = type;
    this.upvotes = upvotes;
    this.downvotes = downvotes;
    this.views = views;
    this.website = website;
  }

  /**
   * Create a date that is a String in the form of mon dd yyyy
   *
   * @param timestamp the timestamp of this listing
   * @return a string that represents the month, day, and year this listing was 
   *     created.
   */
  private String timestampToDate(long timestamp) {
    String dateCreated = createDateFromTimestamp(timestamp);
    int dateLen = dateCreated.length();
    String monthAndDay = dateCreated.substring(4, 7);
    String year = dateCreated.substring(dateLen - 5, dateLen);
    return monthAndDay + year;
  }

  /**
   * Turns a Timestamp object into a Date object
   * 
   * @param timestamp the timestamp of this listing
   * @return a Date object from the Timestamp object
   */
  public static String createDateFromTimestamp(long timestamp) {
    Date date = new Date(timestamp);
    return date.toString();
  }
  
  /**
   * @return location of listing
   */
  public String getLocation(){
    return location;
  }

}
