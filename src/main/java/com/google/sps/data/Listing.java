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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.utility.AuthenticationUtility;
import java.lang.Math;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** A listing */ 
public final class Listing {

  private final String dateCreated;
  private final String description;
  private final String howToHelp;
  private final String imageURL;
  private String key;
  private final String location;
  private final String name;
  private final String type;
  private final int upvotes;
  private final int downvotes;
  private final int views;
  private String vote;
  private final String website;

  public Listing(String description, String howToHelp, String imageURL,
      String keyString, String location, String name, long timestamp,
      String type, int upvotes, int downvotes, int views, String vote, 
      String website) {
    this.description = description;
    this.howToHelp = howToHelp;
    this.key = keyString;
    this.imageURL = imageURL;
    this.location = location;
    this.name = name;

    // turn int of timestamp into Date object
    String dateCreated = timestampToDate(timestamp);
    this.dateCreated = dateCreated;
    this.type = type;
    this.upvotes = upvotes;
    this.downvotes = downvotes;
    this.views = views;
    this.vote = vote;
    this.website = website;
  }

  public Listing(String description, String howToHelp, String imageURL,
      String location, String name, long timestamp, String type, int upvotes,
      int downvotes, int views, String website) {
    this.description = description;
    this.howToHelp = howToHelp;
    this.imageURL = imageURL;
    this.key = "";
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
   * Creates a Listing object from an Entity object that represents a listing
   *
   * @param entity the entity that represents a listing
   * @return a Listing with all of the properties from the Entity
   */
  public static Listing createListing(Entity entity) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    UserService userService = UserServiceFactory.getUserService();

    String description = (String) entity.getProperty("description");
    String howToHelp = (String) entity.getProperty("howToHelp");
    String imageURL = (String) entity.getProperty("imageURL");
    String key = (String) KeyFactory.keyToString(
        entity.getKey());
    String location = (String) entity.getProperty("location");
    String name = (String) entity.getProperty("name");
    long timestamp = (long) entity.getProperty("timestamp");
    String type = (String) entity.getProperty("type");
    int upvotes = Math.toIntExact((long) entity.getProperty("upvotes"));
    int downvotes = Math.toIntExact((long) entity.getProperty("downvotes"));
    int views = Math.toIntExact((long) entity.getProperty("views"));
    String vote = getVoteForListing(datastore, userService, key);
    String website = (String) entity.getProperty("website");

    return new Listing(description, howToHelp, imageURL, key, location, name,
        timestamp, type, upvotes, downvotes, views, vote, website);
  }

  /**
   * Increment a specified property in a listing entity and update it in
   * datastore.
   *
   * @param datastore an instance of datastore service
   * @param listingKey a key associated with a listing entity
   * @param property string of the name of the property to increment
   */
  public static void incrementListingProperty(DatastoreService datastore,
      Key listingKey, String property) throws Exception {
    Entity listingEntity = datastore.get(listingKey);
    long propertyValue = (long) listingEntity.getProperty(property);
    propertyValue++;
    listingEntity.setProperty(property, propertyValue);
    datastore.put(listingEntity);
  }

  /**
   * Decrement a specified property in a listing entity and update it in
   * datastore.
   *
   * @param datastore an instance of datastore service
   * @param listingKey a key associated with a listing entity
   * @param property string of the name of the property to decrement
   */
  public static void decrementListingProperty(DatastoreService datastore,
      Key listingKey, String property) throws Exception {
    Entity listingEntity = datastore.get(listingKey);
    long propertyValue = (long) listingEntity.getProperty(property);
    propertyValue--;
    listingEntity.setProperty(property, propertyValue);
    datastore.put(listingEntity);
  }  

  /**
   * Creates a Listing object from an Entity object that represents a listing
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param listingEntityKey the key of an entity that represents a listing
   * @return a Listing with all of the properties from the Entity
   */
  public static Listing createListing(DatastoreService datastore, 
      Key listingEntityKey) throws Exception {
    Entity listingEntity = datastore.get(listingEntityKey);

    return createListing(listingEntity);
  }  

  /**
   * Creates a Listing object from an Entity object that represents a listing
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param listingEntityKeyString the string of the key of an entity that 
   *     represents a listing
   * @return a Listing with all of the properties from the Entity
   */
  public static Listing createListing(DatastoreService datastore, 
      String listingEntityKeyString) throws Exception {
    Key listingEntityKey = KeyFactory.stringToKey(listingEntityKeyString);

    return createListing(datastore, listingEntityKey);
  }  

  /**
   * Turns a String[] of listing entity key Strings into a List<Listing>.
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param listingEntityKeysStringArray the String[] of listing entity key
   *     Strings that will each be used to create a Listing.
   * @return List<Listing> from the String[] of listing entity key strings.
   */
  public static List<Listing> createListings(DatastoreService datastore, 
      String[] listingEntityKeysStringArray) throws Exception {
    List<Listing> listings = new ArrayList<Listing>();
    for (String listingEntityKeyString : listingEntityKeysStringArray) {
      listings.add(createListing(datastore, listingEntityKeyString));
    }

    return listings;
  }

  /**
   * Get a string representing the vote the current user has on a listing entity.
   *
   * @param datastore a datastore service instance
   * @param userService a user service instance
   * @param listingKeyString a string of a listing entity's key
   * @return a string representing the vote the current user has on a listing entity,
   *     can be upvote, downvote, or neutral
   */
  private static String getVoteForListing(DatastoreService datastore, UserService userService, String listingKeyString) {
    if (userService.isUserLoggedIn()) {
      Entity currentUser = AuthenticationUtility.getCurrentUserEntity(datastore, userService);
      
      String upvotedListings = User.getListingKeysAsString(currentUser, "upvotedListingKeys");
      if (upvotedListings.contains(listingKeyString)) {
        return "upvote";
      }

      String downvotedListings = User.getListingKeysAsString(currentUser, "downvotedListingKeys");
      if (downvotedListings.contains(listingKeyString)) {
        return "downvote";
      }
    }
    return "neutral";
  }

  /**
   * Return location of listing
   * 
   * @return location of listing
   */
  public String getLocation(){
    return location;
  }

  /**
   * Returns the Key String of this listing
   *
   * @return the Key String of this Listing 
   */
  public String getKeyString() {
    return this.key;
  }
}
