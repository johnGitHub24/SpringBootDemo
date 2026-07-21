const puppeteer = require('puppeteer');

(async () => {
  const browser = await puppeteer.launch({ headless: 'new' });
  const page = await browser.newPage();
  
  // Forward browser console logs to Node.js console
  page.on('console', msg => console.log('BROWSER:', msg.text()));

  console.log('Navigating to http://localhost:3000/test/runner.html');
  try {
    await page.goto('http://localhost:3000/test/runner.html', { waitUntil: 'networkidle0' });

    // Wait for the running to be false
    await page.waitForFunction('window.document.querySelector(".btn-run").textContent.includes("重新執行全部測試")', { timeout: 15000 });

    const results = await page.evaluate(() => {
      const cards = document.querySelectorAll('.test-card');
      const failList = [];
      cards.forEach(card => {
        const title = card.querySelector('h5') ? card.querySelector('h5').innerText : card.innerText;
        const passBadge = card.querySelector('.badge-pass');
        const failBadge = card.querySelector('.badge-fail');
        
        if (failBadge) {
          const logs = Array.from(card.querySelectorAll('.log-item')).map(log => log.innerText);
          failList.push({
            title: title.trim(),
            logs: logs
          });
        }
      });
      return failList;
    });

    console.log('--- TEST FAILURE SUMMARY ---');
    if (results.length === 0) {
      console.log('ALL TESTS PASSED!');
    } else {
      results.forEach(res => {
        console.log(`[FAIL] ${res.title}`);
        res.logs.forEach(log => console.log(`   ${log}`));
      });
    }
  } catch (err) {
    console.error('Error during puppeteer test run:', err);
  } finally {
    await browser.close();
  }
})();
