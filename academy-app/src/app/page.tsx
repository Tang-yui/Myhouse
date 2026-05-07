"use client";

import React, { useState } from "react";
import Sidebar from "@/components/Sidebar";
import MapPlaceholder from "@/components/MapPlaceholder";
import StatsDashboard from "@/components/StatsDashboard";
import { mockData } from "@/lib/data";
import { ChevronRight, LayoutDashboard, Map as MapIcon, Settings } from "lucide-react";

export default function Home() {
  const [selectedId, setSelectedId] = useState(mockData[0].id);
  const selectedData = mockData.find((d) => d.id === selectedId) || mockData[0];

  return (
    <div className="flex h-screen bg-slate-50 text-slate-900 font-sans">
      {/* Mini Sidebar / Nav */}
      <div className="w-16 bg-white border-r flex flex-col items-center py-6 gap-8 shadow-sm z-20">
        <div className="w-10 h-10 bg-blue-600 rounded-xl flex items-center justify-center text-white font-bold shadow-lg shadow-blue-200">
          A
        </div>
        <div className="flex flex-col gap-6">
          <button className="p-2 text-blue-600 bg-blue-50 rounded-lg">
            <LayoutDashboard className="w-6 h-6" />
          </button>
          <button className="p-2 text-slate-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors">
            <MapIcon className="w-6 h-6" />
          </button>
          <button className="p-2 text-slate-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors">
            <Settings className="w-6 h-6" />
          </button>
        </div>
      </div>

      {/* Main Sidebar */}
      <Sidebar 
        data={mockData} 
        selectedId={selectedId} 
        onSelect={setSelectedId} 
      />

      {/* Main Content Area */}
      <main className="flex-1 flex flex-col overflow-hidden">
        {/* Top Header */}
        <header className="h-16 bg-white border-b flex items-center justify-between px-8 z-10">
          <div className="flex items-center gap-2 text-sm">
            <span className="text-gray-400">분석</span>
            <ChevronRight className="w-4 h-4 text-gray-300" />
            <span className="font-semibold text-gray-700">{selectedData.name}</span>
          </div>
          <div className="flex gap-3">
            <button className="px-4 py-2 text-xs font-bold text-gray-600 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors">
              리포트 다운로드 (PDF)
            </button>
            <button className="px-4 py-2 text-xs font-bold text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-colors shadow-sm">
              상세 분석 요청
            </button>
          </div>
        </header>

        {/* Scrollable Dashboard Section */}
        <div className="flex-1 overflow-y-auto p-8 space-y-8">
          {/* Hero Section: Map + Brief Stats */}
          <div className="flex flex-col xl:flex-row gap-8">
            <div className="xl:flex-[2] h-[400px]">
              <MapPlaceholder selectedData={selectedData} />
            </div>
            <div className="xl:flex-1 bg-white p-8 rounded-2xl border shadow-sm flex flex-col justify-center">
              <h2 className="text-2xl font-black text-slate-800 leading-tight mb-4">
                {selectedData.name}는<br />
                <span className="text-blue-600">영어학원 입지 최적지</span>입니다.
              </h2>
              <p className="text-gray-500 text-sm leading-relaxed mb-6">
                이 지역은 학령 인구가 밀집되어 있으며, 인근 거주 인원({selectedData.residentPopulation.toLocaleString()}명) 대비 풍부한 수요를 갖추고 있습니다. 특히 {
                  selectedData.schoolAgePopulation.high > selectedData.schoolAgePopulation.elementary ? '고등학생' : '초등학생'
                } 비율이 높아 해당 연령층을 타겟으로 한 영어학원 개업 시 높은 수익성이 기대됩니다.
              </p>
              <div className="space-y-4">
                <div className="flex justify-between items-center text-sm">
                  <span className="text-gray-400">성공 가능성</span>
                  <span className="font-bold text-blue-600">매우 높음</span>
                </div>
                <div className="w-full h-2 bg-gray-100 rounded-full overflow-hidden">
                  <div className="h-full bg-blue-600 rounded-full" style={{ width: `${selectedData.score}%` }}></div>
                </div>
              </div>
            </div>
          </div>

          {/* Detailed Statistics Grid */}
          <section>
            <h2 className="text-lg font-bold text-slate-800 mb-6 flex items-center gap-2">
              상권 심층 분석 데이터
              <span className="text-xs font-normal text-gray-400 ml-2">마지막 업데이트: 오늘</span>
            </h2>
            <StatsDashboard data={selectedData} />
          </section>
        </div>
      </main>
    </div>
  );
}