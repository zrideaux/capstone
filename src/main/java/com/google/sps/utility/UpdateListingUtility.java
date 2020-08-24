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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.sps.utility.ListingConstants;
import com.google.sps.utility.ValidateInput;
import javax.servlet.http.HttpServletRequest;

public class UpdateListingUtility {
  /**
   * Returns true if the current user owns the listing key that is passed as a 
   *     parameter or throws an exception if the user either does not own the 
   *     listing key or if the listing key is invalid.
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param listingKeyString the String that represents the Key of a listing 
   *     Entity.
   * @param userService used to get the user's email.
   * @return a listing Entity if the user owns it.
   */
  public static Entity getListingEntityIfUserOwnsIt(DatastoreService datastore, 
      String listingKeyString, UserService userService) throws Exception {
    if (listingKeyString.length() > 0) {
      if (userService.isUserLoggedIn()) {
        String userEmail = userService.getCurrentUser().getEmail();

        Entity listingEntity = datastore.get(KeyFactory.stringToKey(
            listingKeyString));

        String listingEntityOwnersEmail = (String) listingEntity.getProperty(
            "ownersEmail");

        if (userEmail.equals(listingEntityOwnersEmail)) {
          return listingEntity;
        } else {
          throw new Exception("User does not own this listing");
        }
      } else {
        throw new Exception("User needs to login");
      }
    } else {
      throw new Exception("Missing listing key");
    }
  }

  /**
   * Updates a listing entity.
   *
   * @param datastore the DatastoreService that connects to the back end.
   * @param entity a listing Entity to update.
   * @param request contains data to retrieve params.
   */
  public static void updateListingEntity(DatastoreService datastore,
      Entity entity, HttpServletRequest request) throws Exception {
    String description = ValidateInput.getUserString(request, "description",
        0, ListingConstants.MAX_CONTENT_LEN);

    String howToHelp = ValidateInput.getUserString(request, "howToHelp", 0,
        ListingConstants.MAX_CONTENT_LEN);

    String imageURL = ValidateInput.getUploadedFileUrl(request, "image", "");

    String location = ValidateInput.getUserString(request, "location", 0,
        ListingConstants.MAX_LOCATION_LEN);

    String name = ValidateInput.getUserString(request, "name", 0,
        ListingConstants.MAX_NAME_LEN);

    String type = ValidateInput.getUserString(request, "type", 0,
        ListingConstants.MAX_TYPE_LEN);

    String website = ValidateInput.getParameter(request, "website", "");

    /**
     * If length of the property value is 0 and the property value is the
     *     same as the original value, then keep the original property value.
     */ 
    EntityUtility.updateStringProperty(entity, "description", description);
    EntityUtility.updateStringProperty(entity, "howToHelp", howToHelp);
    EntityUtility.updateStringProperty(entity, "imageURL", imageURL);
    EntityUtility.updateStringProperty(entity, "location", location);
    EntityUtility.updateStringProperty(entity, "name", name);
    EntityUtility.updateStringProperty(entity, "type", type);
    
    entity.setProperty("website", website);
  }
}