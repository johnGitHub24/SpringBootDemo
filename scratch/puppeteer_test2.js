const puppeteer = require('puppeteer');

(async () => {
  const browser = await puppeteer.launch({ 
    headless: 'new',
    args: ['--disable-web-security']
  });
  const page = await browser.newPage();
  
  page.on('console', msg => console.log('BROWSER:', msg.text()));

  const fileUrl = 'file:///d:/MCP/GoldenLuohan/%E9%87%91%E7%BE%85%E6%BC%A2_%E6%89%93%E5%8C%85/%E5%B9%B3%E5%8F%B0/test/runner.html';
  console.log('Navigating to', fileUrl);
  try {
    await page.goto(fileUrl, { waitUntil: 'networkidle0' });

    await page.waitForFunction('window.document.querySelector(".btn-run") && window.document.querySelector(".btn-run").textContent.includes("重新執行全部測試")', { timeout: 15000 });

    const results = await page.evaluate(() => {
      const cards = document.querySelectorAll('.test-card');
      const failList = [];
      cards.forEach(card => {
        const title = card.querySelector('h5') ? card.querySelector('h5').innerText : (card.innerText.split('\n')[0]);
        const passBadge = card.querySelector('.badge-pass');
        const failBadge = card.querySelector('.badge-fail');
        
        if (failBadge || (!passBadge && card.classList.contains('fail'))) {
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
    if (err.name === 'TimeoutError') {
       console.log('Page timed out waiting for results. It might be stuck or failed to run completely.');
       // Dump whatever cards exist
       const results = await page.evaluate(() => {
         const cards = document.querySelectorAll('.test-card');
         return Array.from(cards).map(card => {
           return {
             text: card.innerText.trim().replace(/\n/g, ' | ')
           }
         });
       });
       console.log('Current DOM Test Cards:', results);
    } else {
       console.error('Error during puppeteer test run:', err);
    }
  } finally {
    await browser.close();
  }
})();
