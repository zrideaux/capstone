import { fetchBlobstoreUrlAndSendData } from './../blobstore.js';

import { createListing } from './../listing.js';

import { keyboardAccessibleOnClick } from './../miscellaneous.js';

import { appendParameterToFormData } from './new-listing.js';

import { 
  createCreatePreviewListingFunc,
  createPreviewSendFormDataFunc,
  displayPreviewListing
} from './preview-listing.js';

import { createSubmitSendFormDataFunc } from './submit-listing.js';

/**
 * Initialize the create listing page.
 */
export default function createListingInit(newListingTitle, previewButton, submitButton) {
  newListingTitle.innerHTML = 'Create New Listing';
  keyboardAccessibleOnClick(previewButton, previewNewListing);
  keyboardAccessibleOnClick(submitButton, createNewListing);
}

/**
 * Creates a new listing.
 */
function createNewListing() {
  let queryString = '/create-listing';
  const sendFormDataFunc = createSubmitSendFormDataFunc(
      appendParameterToFormData);
  fetchBlobstoreUrlAndSendData(queryString, sendFormDataFunc);
}

/**
 * Creates a preview of the new listing.
 */
function previewNewListing() {
  let queryString = '/create-listing-preview';

  const previewListingFunc = createCreatePreviewListingFunc(createPreviewListingFunc);

  const sendFormDataFunc = createPreviewSendFormDataFunc(appendParameterToFormData, previewListingFunc);

  displayPreviewListing(queryString, sendFormDataFunc);
}

function createPreviewListingFunc(containerElement, response) {
  containerElement.appendChild(
      createListing('preview-listings', response));
}
