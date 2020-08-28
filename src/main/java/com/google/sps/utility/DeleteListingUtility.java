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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.sps.data.User;
import com.google.sps.utility.ValidateInput;
import java.util.List;

public final class DeleteListingUtility {
  /**
   * Deletes a Listing, and removes its key String from any user's
   *     upvotedListingKey property that upvoted this listing.
   *
   * @param datastore the DatastoreService that connects to 
   *     the database.
   * @param listingEntity the Listing Entity to delete.
   */
  public static void deleteListing(DatastoreService datastore,
      Entity listingEntity) throws Exception {
    Key listingKey = listingEntity.getKey();
    deleteListing(datastore, listingKey);
  }

  /**
   * Deletes a Listing, and removes its key String from any user's
   *     upvotedListingKey property that upvoted this listing.
   *
   * @param datastore the DatastoreService that connects to 
   *     the database.
   * @param listingKey the Key of the Listing to delete.
   */
  public static void deleteListing(DatastoreService datastore, Key listingKey)
      throws Exception {
    String listingKeyString = KeyFactory.keyToString(listingKey);
    deleteListing(datastore, listingKey, listingKeyString);
  }

  /**
   * Deletes a Listing, and removes its key String from any user's
   *     upvotedListingKey property that upvoted this listing.
   *
   * @param datastore the DatastoreService that connects to 
   *     the database.
   * @param listingKeyString the key String of the Listing to delete.
   */
  public static void deleteListing(DatastoreService datastore,
      String listingKeyString) throws Exception {
    Key listingKey = KeyFactory.stringToKey(listingKeyString);
    deleteListing(datastore, listingKey, listingKeyString);
  }

  /**
   * Deletes a Listing, and removes its key String from any user's
   *     upvotedListingKey property that upvoted this listing.
   *
   * @param datastore the DatastoreService that connects to 
   *     the database.
   * @param listingKey the Key of the Listing to delete.
   * @param listingKeyString the key String of the Listing to delete.
   */
  public static void deleteListing(DatastoreService datastore, Key listingKey,
      String listingKeyString) throws Exception {
    // Get all users 
    Query queryUser = new Query("User");
    PreparedQuery preparedQueryUsers = datastore.prepare(queryUser);
    Iterable<Entity> userEntitiesIterable = preparedQueryUsers.asIterable();

    removeUpvotedListingFromUser(datastore, listingKeyString,
        userEntitiesIterable);

    datastore.delete(listingKey);
  }

  /**
   * Removes a Listing's key String from any user's upvotedListingKey property
   *     that upvoted this listing.
   *
   * @param datastore the DatastoreService that connects to 
   *     the database.
   */
  public static void removeUpvotedListingFromUser(DatastoreService datastore,
      String listingKeyString, Iterable<Entity> userEntitiesIterable) {
    // Go through all of the users and remove the upvoted listing key string
    for (Entity userEntity : userEntitiesIterable) {
      String upvotedListingsProperty = "upvotedListingKeys";
      String upvotedListingKeys = (String) userEntity.getProperty(
          upvotedListingsProperty);

      if (upvotedListingKeys.contains(listingKeyString)) {
        upvotedListingKeys = upvotedListingKeys.replaceFirst(
            listingKeyString + " ", "");
        userEntity.setProperty(upvotedListingsProperty, upvotedListingKeys);
        datastore.put(userEntity);
      }
    }
  }
}
