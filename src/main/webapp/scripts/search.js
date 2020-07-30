import createListings from './listing.js';

export default function displayListings(containerElement) {
  containerElement.appendChild(createListings([''], '', 'search-listings'));
}
