document.addEventListener('DOMContentLoaded', // Document Object Model
    () =>
{
    // fetchBtn and message line up with HTML DOM objects
    const fetchBtn = document.getElementById('fetchBtn');
    const message = document.getElementById('message');

    fetchBtn.addEventListener('click',
        () =>
    {
        fetch('/api/hello')
            .then(response => response.text())
            .then(data => {message.textContent = data})
            .catch(err => console.error('Error fetching: ', err));
    });
});