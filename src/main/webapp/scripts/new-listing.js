import getListings from './listing.js';

import {
  createPElement
} from './htmlElement.js';

export default function displayPreviewListing() {
  const containerElement = document.getElementById("preview");
  let queryString = '/create-listing-preview';
  queryString = queryString + getParameterForQuery();
  containerElement.innerHTML = '';

  getListings(containerElement, '', 'preview-listings', queryString);
}

function getParameterForQuery() {
  const name = document.getElementById('cause-name').value;
  const location = document.getElementById('cause-location').value;
  const description = document.getElementById('cause-description').value;
  const howToHelp = document.getElementById('cause-how-to-help').value;
  const website = document.getElementById('cause-website').value;
  console.log("Website: " + website);
  let queryParameters = "?name=" + name;
  queryParameters = queryParameters + "&location=" + location;
  queryParameters = queryParameters + "&description=" + description;
  queryParameters = queryParameters + "&howToHelp=" + howToHelp;
  queryParameters = queryParameters + "&website=" + website;
  queryParameters = queryParameters + "&type=other";
  return queryParameters;
}
