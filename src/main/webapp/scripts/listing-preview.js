import {
  createDeleteElements
} from './delete.js';

import {
  createDivElement, 
  createHElement, 
  createIElement,
  createImgElement, 
  createPElement,
  createSectionElement,
  createSpanElement
} from './htmlElement.js';

import {
  displayErrorMessage,
  isErrorMessage,
  isSuccessMessage,
  keyboardAccessible,
  toggleDisplay,
  toggleElementDisplay
} from './miscellaneous.js';

import { 
  setInitialVoteState,
  voteClicked
} from './reputation.js';

/**
 * Create an element that shows a listing detailed view and when clicked on will
 *     display the detailed view.
 *
 * @param listing JSON that represents a listing and has listing information.
 * @param listingDisplay the display property of the detailed view.
 * @param listingId the id of the listing detailed view element.
 * @param listingPreviewContainer the container for the listing preview element.
 * @return a div with all the preview information pertaining to a listing.
 */

export default function createListingPreview(listing, listingDisplay, listingId,
    listingPreviewContainer) {
  const sectionListing = createSectionElement('listing shadow-box', '');

  const toggleListingDisplay = () => {
    toggleDisplay(listingDisplay, listingId);
  }

  // Make section listing keyboard accessible 
  //     (which includes tabs and enter keys)
  keyboardAccessible(sectionListing, toggleListingDisplay, toggleListingDisplay,
      "0");

  // Creating listing information
  const imageURL = listing.imageURL;
  const upvotes = listing.upvotes.toLocaleString();
  const downvotes = listing.downvotes.toLocaleString();
  const key = listing.key;
  const vote = listing.vote;
  sectionListing.appendChild(createListingInformation(downvotes, imageURL, key, upvotes, vote));

  // Creating listing details
  const description = listing.description;
  const isOwnerUser = listing.isOwnerUser;
  const location = listing.location;
  const name = listing.name;
  const type = listing.type;
  sectionListing.appendChild(createListingDetails(description, isOwnerUser, key,
      listingPreviewContainer, location, name, type));

  return sectionListing;
}

/**
 * Create an element with listing information
 *
 * @param imageURL the source of the listing image
 * @param upvotes the number of upvotes this listing has received
 * @return a div with the picture and the number of upvotes this listing has
 */
function createListingInformation(downvotes, imageURL, key, upvotes, vote) {
  const divListingInfo = createDivElement('', 'preview-info', '');

  divListingInfo.appendChild(
    createImgElement(imageURL, 'Listing preview image', 'listing-image', ''));

  divListingInfo.appendChild(
      createPreviewReputationContainer(downvotes, vote, key, upvotes));

  return divListingInfo;
}

/**
 * Create an element with listing details.
 *
 * @param description the description associated with this listing.
 * @param isOwnerUser a boolean that states whether or not the user owns this 
 *     listing.
 * @param key the key of this listing.
 * @param listingPreviewContainer the container for the listing preview element.
 * @param location the location of this listing.
 * @param name the name of this listing.
 * @param type the type of the listing .
 * @return a div with the description, name, and tags of a listing.
 */
function createListingDetails(description, isOwnerUser, key,
    listingPreviewContainer, location, name, type) {
  const divListingDetails = createDivElement('', 'listing-info-container', '');

  // Creating listing heading
  divListingDetails.appendChild(createListingHeading(isOwnerUser, key,
      listingPreviewContainer, name, type));

  divListingDetails.appendChild(
    createPElement(location, 'listing-preview-details', ''));

  divListingDetails.appendChild(
    createPElement(description, 'listing-preview-details', ''));

  return divListingDetails;
}

/**
 * Create an element with listing heading.
 *
 * @param isOwnerUser a boolean that states whether or not the user owns this
 *     listing.
 * @param key the key of this listing.
 * @param listingPreviewContainer the container for the listing preview element.
 * @param name the name of this listing.
 * @param type the type of the listing.
 * @return a div with the name and tags of a listing.
 */
function createListingHeading(isOwnerUser, key, listingPreviewContainer, name,
    type) {
  const divListingHeading = createDivElement('', 'listing-heading-container', 
      '');
  
  divListingHeading.appendChild(
    createHElement(name, 2, 'listing-preview-name', ''));
  
  // Group the edit and type together
  const divListingSubHeading = createDivElement('', 'listing-sub-heading', '');
  divListingHeading.appendChild(divListingSubHeading);

  if (isOwnerUser) {
    divListingSubHeading.appendChild(createEdit(key));

    const deleteOnSuccessFunc = createRemoveListingFunc(
        listingPreviewContainer);
    
    const queryString = 'delete-listing?listing-key=' + key;
    divListingSubHeading.appendChild(createDeleteElements('listing-delete',
        deleteOnSuccessFunc, name, queryString));
  }

  divListingSubHeading.appendChild(createListingType(type));

  return divListingHeading;
}

/**
 * Used for when a listing is deleted.
 * Creates a function that removes the listing element from the page.
 * If this is the last listing element, then add a <p> saying 'No listings'.
 *
 * @param listingPreviewContainer the container element for the preview listing.
 * @return a function that removes the listing element from the page.
 */
function createRemoveListingFunc(listingPreviewContainer) {
  return () => {
    const listingsContainer = listingPreviewContainer.parentElement;
    const numListings = listingsContainer.childElementCount;
    listingPreviewContainer.remove();
    if (numListings == 1) {
      listingsContainer.appendChild(createPElement('No listings', '', ''));
    }
  }
}

/**
 * Create an edit element. 
 *
 * @param key the key of this listing.
 * @return A p element that represents an edit element.
 */
function createEdit(key) {
  const editPElement = createPElement('Edit', 'listing-edit', '');
  const moveToEditListingPage = (event) => { 
    editListingPageUrl(key);
    event.stopPropagation();
  };
  keyboardAccessible(editPElement, moveToEditListingPage, moveToEditListingPage);

  return editPElement;
}

/**
 * Verifies that the user has access to this listing and route them to the new 
 *     listing page if they do.
 *
 * @param key the key of this listing.
 */
function editListingPageUrl(key) {
  const query = '/fetch-listing?listing-key=' + key;

  fetch(query)
      .then(response => response.json())
      .then((listing) => {
        if(isErrorMessage(listing)) {
          displayErrorMessage(listing);
        } else {
          location.replace('/newlisting.html?key=' + listing.key);
        }
      });
}

/**
 * Create an element with listing type
 *
 * @param type the type of the listing 
 * @return a div with the type of a listing
 */
function createListingType(type) {
  const divListingType = createDivElement('', 'listing-tags-container', '');

  divListingType.appendChild(createSpanElement(type, 'listing-tag', ''));

  return divListingType;
}

/**
 * Creates a container that holds uniquely id'd downvote and upvote button
 * for a listing preview.
 *
 * @param downvotes an int representing the number of downvotes a listing has
 * @param existingVote the vote which the current user already has on a listing
 * @param key a string that is the key of a listing
 * @param upvotes an int representing the number of upvotes a listing has
 * @return a div element display a listing preview's reputation
 */
function createPreviewReputationContainer(downvotes, existingVote, key, upvotes) {
  let reputationContainer = createDivElement('', '', 'reputation-preview-container');

  // Create div with the number of upvotes and upvote icon
  let upvoteDiv = createDivElement('', 'reputation-preview',
      'reputation-upvote-preview-' + key);
  let upvoteIcon = createIElement('thumb_up', 'material-icons', '');
  let upvoteCount = createSpanElement(upvotes, 'reputation-count', 'reputation-preview-count-upvotes-' + key);
  upvoteDiv.appendChild(upvoteIcon);
  upvoteDiv.appendChild(upvoteCount);
  
  // Create div with the number of downvotes and downvote icon
  let downvoteDiv = createDivElement('', 'reputation-preview',
      'reputation-downvote-preview-' + key);
  let downvoteIcon = createIElement('thumb_down', 'material-icons', '');
  let downvoteCount = createSpanElement(downvotes, 'reputation-count', 'reputation-preview-count-downvotes-' + key);
  downvoteDiv.appendChild(downvoteIcon);
  downvoteDiv.appendChild(downvoteCount);

  // Set the initial state of the buttons when they're made
  setInitialVoteState(downvoteDiv, upvoteDiv, existingVote, key);

  reputationContainer.appendChild(upvoteDiv);
  reputationContainer.appendChild(downvoteDiv);

  return reputationContainer;
}
