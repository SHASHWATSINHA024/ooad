// Custom JavaScript for the application
document.addEventListener('DOMContentLoaded', function() {
    // Tab switching logic for dashboards
    const tabs = ['overview', 'users', 'books', 'transactions', 'reports', 'settings'];
    tabs.forEach(tab => {
        const tabElement = document.getElementById(tab + '-tab');
        if (tabElement) {
            tabElement.addEventListener('click', function(e) {
                e.preventDefault();
                // Hide all content
                tabs.forEach(t => {
                    const content = document.getElementById(t + '-content');
                    if (content) content.style.display = 'none';
                    const tabEl = document.getElementById(t + '-tab');
                    if (tabEl) tabEl.classList.remove('active');
                });
                // Show selected content
                const content = document.getElementById(tab + '-content');
                if (content) content.style.display = 'block';
                this.classList.add('active');
            });
        }
    });
});