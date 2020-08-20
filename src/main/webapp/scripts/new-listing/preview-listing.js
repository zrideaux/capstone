import {
  fetchBlobstoreUrlAndSendData,
  sendFormData,
} from './../blobstore.js';

import { createListing } from './../listing.js';

/**
 * Creates a listing preview for the user to see.
 */
export default function displayPreviewListing() {
  const containerElement = document.getElementById("preview");
  let queryString = '/create-listing-preview';
  containerElement.innerHTML = '';

  fetchBlobstoreUrlAndSendData(queryString, sendPreviewListingFormData);

  window.location.href = '#preview-listings';
}

/**
 * Send new-listing form and input related data to the imageUploadURL and 
 *     create a preview with the response from the imageUploadURL servlet.
 * 
 * @param imageUploadURL the url to send listing data to.
 */
function sendPreviewListingFormData(imageUploadURL) {
  const newListingForm = document.getElementById('new-listing-form');
  sendFormData(appendParameterToFormData, newListingForm, imageUploadURL, 
      createPreviewListing);
}

/**
 * Creates a listing given a Listing JSON and appends the listing the the 
 *     preview container element in the newlisting page.
 *
 * @param listing JSON that represents a Listing object.
 */
function createPreviewListing(listing) {
  const containerElement = document.getElementById("preview");
  containerElement.appendChild(
      createListing('preview-listings', listing));

  $(document).ready(function(){
    const $preview = $('#preview');
    const bottom = $preview.position().top + $preview.offset().top + 
        $preview.outerHeight(true);
    $('html, body').animate({
        scrollTop: bottom
    }, 900);
  });
}