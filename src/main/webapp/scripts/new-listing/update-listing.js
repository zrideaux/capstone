import { fetchBlobstoreUrlAndSendData } from './../blobstore.js';

import { keyboardAccessibleOnClick } from './../miscellaneous.js';

import { sendNewListingFormData } from './submit-listing.js';

/**
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
  console.log("POPULATE input elements.");
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

function updatePreview() {

}
