
/**
 * Reveal a specified dropdown menu when called. 
 */
function showDropdown(menuName) {
  // Hide any other open dropdown menus
  const allDropdowns = document.getElementsByClassName('dropdown-menu');
  for (let i = 0; i < allDropdowns.length; i ++) {
    allDropdowns[i].style.display = 'none';
  }

  // Display specified menu
  const dropdownMenu = document.getElementById(menuName);
  dropdownMenu.style.display = 'block';
}