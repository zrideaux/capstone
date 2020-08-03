import { createListing } from './listing.js';

import { 
  isErrorMessage,
  displayErrorMessage
} from './miscellaneous.js';

/**
 * Creates a listing preview for the user to see
 */
export default function displayPreviewListing() {
  const containerElement = document.getElementById("preview");
  let queryString = '/create-listing-preview';
  // queryString = queryString + getParameterForQuery();
  containerElement.innerHTML = '';

  fetchBlobstoreUrl(queryString);
}

function createPreviewListing(listing) {
  const containerElement = document.getElementById("preview");
  containerElement.appendChild(
      createListing('preview-listings', listing));
}

function fetchBlobstoreUrl(servletUrl) {
  const queryString = "/blobstore-upload-url?servlet-url=" + servletUrl;
  fetch(queryString)
      .then(response => response.json())
      .then((imageUploadUrl) => {
        if(isErrorMessage(imageUploadUrl)) {
          displayErrorMessage(imageUploadUrl);
        } else {
          console.log("Send form data.")
          const newListingForm = document.getElementById("new-listing-form");
          sendFormData(newListingForm, imageUploadUrl);
        }
      })
}

/** 
 * Sends name and content input data, and it clears the input elements
 * 
 * @param nameInputId the id of the input element with the name data
 * @param contentInputId the id of the input element with the content data
 */
function sendFormData(commentForm, imageUploadUrl) {
  const data = new FormData(commentForm);

  // Add parameters to the formData
  appendParameterToFormData(data);

  // prints the input name and value
  for (var key of data.entries()) {
    console.log(key[0] + ', ' + key[1]);
  }

  const req = new XMLHttpRequest();
  req.open("POST", imageUploadUrl, true);
  req.onload = function() {
    // when response is ready and status is ok
    if (req.status == 200) {
      console.log("Response text: " + req.responseText);
      const response = JSON.parse(req.responseText);
      if (isErrorMessage(response)) {
        displayErrorMessage(response);
      } else {
        console.log("Uploaded!");
        const containerElement = document.getElementById("preview");
        containerElement.appendChild(
            createListing('preview-listings', response));
      }
    } else {
      const errorMessage = "Error " + req.status + 
          " occurred when trying to upload your form.";
      displayErrorMessage(errorMessage);
    }
  };
  req.onerror = function() {
    const errorMessage = "An error occurred during the transaction,";
    displayErrorMessage(errorMessage);
  };
  req.send(data);
}

/**
 * Creates a string of parameters to query a servlet
 *
 * @return creates a string of parameters with their values.
 */
function getParameterForQuery() {
  const name = document.getElementById('cause-name').value;
  const location = document.getElementById('cause-location').value;
  const description = document.getElementById('cause-description').value;
  const howToHelp = document.getElementById('cause-how-to-help').value;
  const website = document.getElementById('cause-website').value;
  console.log("Website: " + website);
  let queryParameters = "?name=" + name;
  queryParameters = queryParameters + "&type=" + getType();
  queryParameters = queryParameters + "&location=" + location;
  queryParameters = queryParameters + "&description=" + description;
  queryParameters = queryParameters + "&howToHelp=" + howToHelp;
  queryParameters = queryParameters + "&website=" + website;
  return queryParameters;
}

/**
 * Creates a string of parameters to query a servlet
 * 
 * @param formData the formData to append parameters to that will be sent to 
 *     servlet
 */
function appendParameterToFormData(formData) {
  const name = document.getElementById('cause-name').value;
  const location = document.getElementById('cause-location').value;
  const description = document.getElementById('cause-description').value;
  const howToHelp = document.getElementById('cause-how-to-help').value;
  const website = document.getElementById('cause-website').value;
  formData.append('name', name);
  formData.append('type', getType());
  formData.append('location', location);
  formData.append('description', description);
  formData.append('howToHelp', howToHelp);
  formData.append('website', website);
}

/**
 * Gets the type of the the listing chosen in the form.
 *
 * @return the value of the radio type or an empty sting if none have been 
 *     chosen.
 */
function getType() {
  const radioTypes = document.getElementsByName("cause-type");
  for (let i = 0; i < radioTypes.length; i++) {
    const radioType = radioTypes[i];
    if (radioType.checked) {
      return radioType.value;
    }
  }
  return "";
}
