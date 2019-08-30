const UPDATE_INTERVAL = 10; // 10초마다 글자 업데이트
const SECOND = "second";

// 가벼운 시간 라이브러리인 Day.js 사용 https://github.com/iamkun/dayjs
// 전역 한국어 로케일 설정 및 상대 시간 출력 플러그인 적용
dayjs.locale("ko");
dayjs.extend(dayjs_plugin_relativeTime);

let serverTime;

const getServerTime = () => {
    fetch("/datetime")
        .then(response => response.json())
        .then(datetime => { serverTime = dayjs(datetime.datetime); })
        .catch(ignored => { serverTime = dayjs(); }); // fallback
};

const addIntervalToServerTime = () => {
    if (!serverTime) getServerTime();
    else serverTime = dayjs(serverTime).add(UPDATE_INTERVAL, SECOND);
};

const updateTimeStrings = () => {
    const timeStamps = document.getElementsByTagName("time");
    for (let timeStamp of timeStamps) {
        timeStamp.textContent = dayjs(timeStamp.dateTime)
            .subtract(UPDATE_INTERVAL, SECOND)
            .from(serverTime);
    }
};

const updateTimeStringsInterval = () => {
    addIntervalToServerTime();
    updateTimeStrings();
};

setInterval(updateTimeStringsInterval, UPDATE_INTERVAL * 1000);