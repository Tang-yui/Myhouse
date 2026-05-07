"use client";

import React, { useState } from "react";
import { Search, MapPin, GraduationCap, TrendingUp, Users } from "lucide-react";
import { EnglishAcademyStats } from "@/lib/data";

interface SidebarProps {
  data: EnglishAcademyStats[];
  selectedId: string;
  onSelect: (id: string) => void;
}

export default function Sidebar({ data, selectedId, onSelect }: SidebarProps) {
  const [searchQuery, setSearchQuery] = useState("");

  const filteredData = data.filter((item) =>
    item.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="w-80 bg-white border-r h-full flex flex-col shadow-sm">
      <div className="p-6 border-b">
        <h1 className="text-xl font-bold text-blue-600 flex items-center gap-2">
          <GraduationCap className="w-6 h-6" />
          학원 상권 분석
        </h1>
        <div className="mt-4 relative">
          <input
            type="text"
            placeholder="지역 또는 학원 검색..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-10 pr-4 py-2 bg-gray-100 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 transition-all"
          />
          <Search className="absolute left-3 top-2.5 w-4 h-4 text-gray-400" />
        </div>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-3">
        <h2 className="text-xs font-semibold text-gray-500 uppercase tracking-wider px-2">
          {searchQuery ? `검색 결과 (${filteredData.length})` : "추천 분석 지역"}
        </h2>
        {filteredData.length > 0 ? (
          filteredData.map((item) => (
            <button
              key={item.id}
              onClick={() => onSelect(item.id)}
              className={`w-full text-left p-4 rounded-xl transition-all ${
                selectedId === item.id
                  ? "bg-blue-50 border-blue-200 border shadow-sm"
                  : "hover:bg-gray-50 border-transparent border"
              }`}
            >
              <div className="flex justify-between items-start mb-2">
                <span className={`font-semibold ${selectedId === item.id ? "text-blue-700" : "text-gray-700"}`}>
                  {item.name}
                </span>
                <span className="text-xs font-bold bg-blue-100 text-blue-600 px-2 py-1 rounded">
                  {item.score}점
                </span>
              </div>
              <div className="flex items-center gap-3 text-xs text-gray-500">
                <span className="flex items-center gap-1">
                  <Users className="w-3 h-3" />
                  {(item.schoolAgePopulation.elementary + item.schoolAgePopulation.middle + item.schoolAgePopulation.high).toLocaleString()}명
                </span>
                <span className="flex items-center gap-1">
                  <TrendingUp className="w-3 h-3" />
                  경쟁 {item.englishAcademiesCount}개
                </span>
              </div>
            </button>
          ))
        ) : (
          <div className="p-8 text-center">
            <p className="text-sm text-gray-400 italic">검색 결과가 없습니다.</p>
          </div>
        )}
      </div>

      <div className="p-4 bg-gray-50 border-t text-[10px] text-gray-400 text-center">
        © 2026 Academy Analyzer Pro. All rights reserved.
      </div>
    </div>
  );
}
