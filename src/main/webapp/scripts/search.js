import { getListings } from './listing.js';

export default function displayListings(containerElement) {
  const exListingKeys = [];
  const queryString = '/fetch-user-listings?listing-keys=' + exListingKeys;
  containerElement.appendChild(getListings(containerElement, '', 'search-listings', queryString));
}
