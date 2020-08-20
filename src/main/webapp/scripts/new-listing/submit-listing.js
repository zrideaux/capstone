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

/**
 * Create a sendFormDataFunc to pass into fetchBlobstoreUrlAndSendData func in 
 *     blobstore.js.
 * 
 * @param sendFormDataFunc the function that will append parameters to a form 
 *     data.
 * @return a func that will send the new-listing form and input related data to 
 *     the imageUploadURL to create/update a new listing and then redirect the 
 *     user to the user page.
 */
function createSendFormDataFunc(appendDataFunc =
    appendParameterToFormData) {
  return (imageUploadURL) => {
    const newListingForm = document.getElementById("new-listing-form");
    sendFormData(appendDataFunc, newListingForm, imageUploadURL, 
        redirectToUserPage);
  }
}

export {
  createSendFormDataFunc,
  redirectToUserPage,
  sendNewListingFormData,
}
