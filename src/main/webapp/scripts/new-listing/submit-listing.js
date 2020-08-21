import { sendFormData } from './../blobstore.js';

/**
 * Redirect the user to the user page.
 */
function redirectToUserPage() {
  location.replace('user.html');
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
function createSubmitSendFormDataFunc(appendDataFunc) {
  return (imageUploadURL) => {
    const newListingForm = document.getElementById("new-listing-form");
    sendFormData(appendDataFunc, newListingForm, imageUploadURL, 
        redirectToUserPage);
  }
}

export { createSubmitSendFormDataFunc };
