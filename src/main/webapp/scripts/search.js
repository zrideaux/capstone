
import { getListings } from './listing.js';
import { authenticate } from './authentication.js';

window.onload = function() {
  authenticate();
}

export default function displayListings(containerElement) {
  const queryString = '/fetch-listings';
  getListings(containerElement, '', 'search-listings', queryString);
}
