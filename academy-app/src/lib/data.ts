export interface EnglishAcademyStats {
  id: string;
  name: string;
  location: { lat: number; lng: number };
  schoolAgePopulation: {
    kindergarten: number;
    elementary: number;
    middle: number;
    high: number;
  };
  residentPopulation: number; // 인근 거주 인원
  englishAcademiesCount: number; // 영어학원 갯수
  topAcademies: string[]; // 1-10등 영어학원
  pros: string[]; // 장점
  cons: string[]; // 단점
  averageIncome: number;
  score: number;
}

export const mockData: EnglishAcademyStats[] = [
  {
    id: "1",
    name: "강남구 대치동 상권",
    location: { lat: 37.4967, lng: 127.063 },
    schoolAgePopulation: {
      kindergarten: 1200,
      elementary: 4500,
      middle: 3800,
      high: 5200,
    },
    residentPopulation: 85000,
    englishAcademiesCount: 120,
    topAcademies: [
      "정상어학원 대치본원",
      "청담어학원 대치브랜치",
      "최선어학원 대치캠퍼스",
      "이루어진영어학원",
      "피아이어학원",
      "KAGE영재교육원",
      "해커스어학원 (중고등)",
      "아발론교육 대치",
      "폴리어학원 대치",
      "파고다어학원 강남",
    ],
    pros: [
      "국내 최대 규모의 사교육 수요와 높은 교육열",
      "고소득 가구 밀집으로 프리미엄 학원 운영에 유리",
      "특목고/자사고 및 명문대 진학을 목표로 하는 우수 학생 다수",
    ],
    cons: [
      "이미 대형 프랜차이즈 및 유명 학원들이 선점하여 경쟁이 매우 치열함",
      "임대료 및 초기 인테리어 등 초기 진입 비용이 전국 최고 수준",
      "강사 구인 및 인건비 부담이 큼",
    ],
    averageIncome: 650,
    score: 88,
  },
  {
    id: "2",
    name: "양천구 목동 상권",
    location: { lat: 37.5301, lng: 126.8648 },
    schoolAgePopulation: {
      kindergarten: 950,
      elementary: 3800,
      middle: 3200,
      high: 4100,
    },
    residentPopulation: 72000,
    englishAcademiesCount: 85,
    topAcademies: [
      "청담어학원 목동브랜치",
      "정상어학원 목동분원",
      "씨앤씨어학원 목동본원",
      "최선어학원 목동캠퍼스",
      "아발론교육 목동",
      "폴리어학원 목동",
      "에이프릴어학원 목동",
      "이투스247학원 목동",
      "명인학원 목동",
      "하이스트학원 목동",
    ],
    pros: [
      "전통적인 교육 특구로 학부모들의 학원 정보 교류가 활발함",
      "초중등 위주의 탄탄한 수요층 확보 가능",
      "대단지 아파트 밀집으로 도보 통원 비율이 높음",
    ],
    cons: [
      "특정 대형 학원으로의 쏠림 현상이 심함",
      "학부모들의 입소문에 매우 민감하여 초기 원생 확보에 시간이 소요될 수 있음",
      "내신 대비 비중이 높아 학교별 맞춤 커리큘럼 필수",
    ],
    averageIncome: 520,
    score: 92,
  },
  {
    id: "3",
    name: "노원구 중계동 상권",
    location: { lat: 37.6481, lng: 127.0763 },
    schoolAgePopulation: {
      kindergarten: 800,
      elementary: 3200,
      middle: 2900,
      high: 3500,
    },
    residentPopulation: 68000,
    englishAcademiesCount: 65,
    topAcademies: [
      "청담어학원 중계브랜치",
      "정상어학원 중계분원",
      "최선어학원 중계캠퍼스",
      "토피아어학원 중계",
      "아발론교육 중계",
      "폴리어학원 중계",
      "에이프릴어학원 중계",
      "세일학원",
      "명인학원 중계",
      "학림학원",
    ],
    pros: [
      "강북 지역 최대 교육 특구로 안정적인 수요 존재",
      "대치, 목동 대비 상대적으로 저렴한 임대료",
      "중저가~중고가 사이의 가성비 좋은 학원 선호도 높음",
    ],
    cons: [
      "대치, 목동 대비 프리미엄(고가) 과정 수요는 다소 제한적",
      "대중교통 접근성이 타 교육 특구 대비 떨어져 셔틀버스 운행이 필수적인 경우가 많음",
    ],
    averageIncome: 410,
    score: 85,
  },
];