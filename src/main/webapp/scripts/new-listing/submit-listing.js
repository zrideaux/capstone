import { sendFormData } from './../blobstore.js';

import { appendParameterToFormData } from './new-listing.js';

/**
 * Redirect the user to the user page.
 */
function redirectToUserPage() {
  location.replace('user.html');
}

/**
 * Send new-listing form and input related data to the imageUploadURL to create 
 *     a new listing and then redirect the user to the user page.
 * 
 * @param imageUploadURL the url to send listing data to.
 */
function sendNewListingFormData(imageUploadURL) {
  const newListingForm = document.getElementById("new-listing-form");
  sendFormData(appendParameterToFormData, newListingForm, imageUploadURL, 
      redirectToUserPage);
}

export {
  redirectToUserPage,
  sendNewListingFormData,
}
