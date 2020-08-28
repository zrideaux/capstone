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
import com.google.sps.data.User;
import com.google.sps.utility.DeleteListingUtility;
import java.util.List;

public final class DeleteUserUtility {
  /**
   * Delete all of a user's created listings.
   *
   * @param datastore the DatastoreService that connects to 
   *     the database.
   * @param userEmail the email of the user whose created listings will be 
   *     deleted.
   */
  public static void deleteUsersCreatedListings(DatastoreService datastore,
      String userEmail) throws Exception {
    List<Entity> ownedListingEntities = User.getCreatedListingEntities(
        datastore, userEmail);

    for (Entity listingEntity : ownedListingEntities) {
      DeleteListingUtility.deleteListing(datastore, listingEntity);
    }
  }
}
