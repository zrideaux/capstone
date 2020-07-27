
import createListing from "/listing.js";

export default function createListings(containerElement) {
  for (let i = 0; i < 1; i ++) {
    containerElement.appendChild(createListing(i + 1));
  }
}
