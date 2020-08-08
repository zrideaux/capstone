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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Listing;
import com.google.sps.data.User;
import com.google.sps.utility.AuthenticationUtility;
import com.google.sps.utility.ValidateInput;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reputation")
public class Reputation extends HttpServlet {
  /**
   * Updates user and listing entities' reputation related properties.
   *
   * @param request an http request to the servlet
   * @param response the http response sent 
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      String listingKeyString = ValidateInput.getParameter(request, "key", "");
      String vote = ValidateInput.getParameter(request, "vote", "");

      // Make sure vote is a valid type.
      if (voteIsInvalid(vote)) {
        String errorMessage = "'" + vote + "' is an invalid vote type.";
        ValidateInput.createErrorMessage(errorMessage, response);  
        return;
      }

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Entity currentUser = AuthenticationUtility.getCurrentUserEntity(datastore,
          userService);

      processVote(datastore, currentUser, listingKeyString, vote);
    } else {
      ValidateInput.createErrorMessage("User is not logged in.", response);
    }
  }

  /**
   * Process a vote for a listing by updating the relevant datastore properties
   * of a listing and a user.
   *
   * @param datastore a datastore service instance
   * @param userEntity an entity representing a User
   * @param listingKey the key representing a specific Listing entity
   * @param vote a string representing the type of vote to received 
   */
  private void processVote(DatastoreService datastore, Entity userEntity, 
      String listingKeyString, String vote) {

    String userUpvotes = User.getListingKeysAsString(userEntity, "upvotedListingKeys");
    String userDownvotes = User.getListingKeysAsString(userEntity, "downvotedListingKeys");
    Key listingKey = KeyFactory.stringToKey(listingKeyString);

    if (userUpvotes.contains(listingKeyString)) {  
      // If a listing is already present in a user's upvotes when this function
      //    is called, they are changing their vote (to downvote or neutral)
      removeVote(datastore, userEntity, listingKey, "upvote");
      
      // Check if their new vote was an upvote, and add it to the user and the
      //    listing if it was.
      addVote(datastore, userEntity, listingKey, vote);
    } else if (userDownvotes.contains(listingKeyString)) {
      // If a listing is already present in a user's downvotes when this function
      //    is called, they are changing their vote (to upvote or neutral)
      removeVote(datastore, userEntity, listingKey, "downvote");

      // Check if their new vote was an upvote, and add it to the user and the
      //    listing if it was.
      addVote(datastore, userEntity, listingKey, vote);
    } else {
      // If the user's vote was previously neutral, just add their new vote
      addVote(datastore, userEntity, listingKey, vote);
    }
  }

  /**
   * Add a listing key to one of a User's vote properties and increment
   * the respective property of the listing entity.
   *
   * @param datastore a datastore service instance
   * @param userEntity an entity representing a User
   * @param listingKey the key representing a specific Listing entity
   * @param vote a string representing the type of vote to add to the user
   *    and the listing being interacted with 
   */
  private void addVote(DatastoreService datastore, Entity userEntity,
      Key listingKey, String vote) {
    if (vote.equals("upvote")) {
      User.addListingKeyToUserEntity(datastore, userEntity, 
          listingKey, "upvotedListingKeys");
      Listing.incrementListingProperty(datastore, listingKey, "upvotes");
    } else if (vote.equals("downvote")) {
      User.addListingKeyToUserEntity(datastore, userEntity, 
          listingKey, "downvotedListingKeys");
      Listing.incrementListingProperty(datastore, listingKey, "downvotes");
    }
  }

  /**
   * Remove a listing key from one of a User's vote properties and decrement
   * the respective property of the listing entity.
   *
   * @param datastore a datastore service instance
   * @param userEntity an entity representing a User
   * @param listingKey the key representing a specific Listing entity
   * @param vote a string representing the type of vote to remove from
   *    the user and the listing being interacted with 
   */
  private void removeVote(DatastoreService datastore, Entity userEntity, 
      Key listingKey, String vote) {
    if (vote.equals("upvote")) {
      User.removeListingKeyFromUserEntity(datastore, userEntity, 
          listingKey, "upvotedListingKeys");
      Listing.decrementListingProperty(datastore, listingKey, "upvotes");
    } else if (vote.equals("downvote")) {
      User.removeListingKeyFromUserEntity(datastore, userEntity, 
          listingKey, "downvotedListingKeys");
      Listing.decrementListingProperty(datastore, listingKey, "downvotes");
    }
  }

  /**
   * Determine whether or not an input vote is invalid.
   *
   * @param vote a string representing the type of vote received
   * @return boolean whose value is true if the vote string isn't valid
   */
  private boolean voteIsInvalid(String vote) {
    if (!vote.equals("upvote")
        && !vote.equals("downvote")
        && !vote.equals("neutral")) {
      return true;
    }
    return false;
  }

  /**
   * Generate and return a reputation score from a listing entity)
   * 
   * @param listingEntity the entity of the listing to generate a score for
   * @return int representing a listing's reputationScore
   */
  public static int generateReputationScore(Entity listingEntity) {
    final double WEIGHT_1 = .20;
    final double WEIGHT_2 = .80;
    
    int upvotes = (int) new Long(
        (long) listingEntity.getProperty("upvotes")).intValue();
    int downvotes = (int) new Long(
        (long) listingEntity.getProperty("downvotes")).intValue();
    double upvotePercentage = upvotes / (upvotes + downvotes);

    int reputationScore = (int) ((upvotes * WEIGHT_1) 
        + ((upvotes * upvotePercentage) * WEIGHT_2));

    return reputationScore;
  }
}
