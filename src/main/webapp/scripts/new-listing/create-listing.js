import { fetchBlobstoreUrlAndSendData } from './../blobstore.js';

import { keyboardAccessibleOnClick } from './../miscellaneous.js';

import displayPreviewListing from './preview-listing.js';

import { sendNewListingFormData } from './submit-listing.js';

export default function createListingInit(previewButton, submitButton) {
  keyboardAccessibleOnClick(previewButton, displayPreviewListing);
  keyboardAccessibleOnClick(submitButton, createNewListing);
}

/**
 * Creates a new listing.
 */
function createNewListing() {
  let queryString = '/create-listing';
  fetchBlobstoreUrlAndSendData(queryString, sendNewListingFormData);
}
