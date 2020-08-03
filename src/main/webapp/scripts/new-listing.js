import getListings from './listing.js';

/**
 * Creates a listing preview for the user to see
 */
export default function displayPreviewListing() {
  const containerElement = document.getElementById("preview");
  let queryString = '/create-listing-preview';
  queryString = queryString + getParameterForQuery();
  containerElement.innerHTML = '';

  getListings(containerElement, '', 'preview-listings', queryString);
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
