import { fetchBlobstoreUrlAndSendData } from './../blobstore.js';

import { keyboardAccessibleOnClick } from './../miscellaneous.js';

import { sendNewListingFormData } from './submit-listing.js';

/**
 * Initialize the update listing page.
 * Populate input elements with listings properties, and add functions to the 
 *     preview and submit button.
 */
export default function updateListingInit(listing, previewButton, submitButton) {
  populateInputWithListingInfo(listing);
  keyboardAccessibleOnClick(previewButton, updatePreview);
  keyboardAccessibleOnClick(submitButton, updateListing);
}

/**
 * This function will populate the input fields with information/values from 
 *     the listing that will be edited.
 *
 * @param listing JSON that represents a Listing
 */
function populateInputWithListingInfo(listing) {
  document.getElementById('cause-name').value = listing.name;
  document.getElementById(listing.type + '-radio').checked = true;
  document.getElementById('cause-location').value = listing.location;
  document.getElementById('cause-description').value = listing.description;
  document.getElementById('cause-how-to-help').value = listing.howToHelp;
  if (listing.website.length > 0) {
    document.getElementById('cause-website').value = listing.website;
  }
}

/**
 * Updates the Listing.
 */
function updateListing() {
  let queryString = '/update-listing';
  fetchBlobstoreUrlAndSendData(queryString, sendNewListingFormData);
}

/**
 * Creates an update preview listing.
 */
function updatePreview() {
  let queryString = '/update-listing-preview';
  // create an update listing preview
}
