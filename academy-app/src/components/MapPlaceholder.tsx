"use client";

import React from "react";
import { MapPin } from "lucide-react";
import { EnglishAcademyStats } from "@/lib/data";

interface MapPlaceholderProps {
  selectedData: EnglishAcademyStats;
}

export default function MapPlaceholder({ selectedData }: MapPlaceholderProps) {
  return (
    <div className="relative w-full h-full bg-slate-200 overflow-hidden rounded-2xl border border-slate-300 shadow-inner group">
      {/* Abstract Map Grid Pattern */}
      <div 
        className="absolute inset-0 opacity-20"
        style={{
          backgroundImage: `radial-gradient(#334155 0.5px, transparent 0.5px)`,
          backgroundSize: '24px 24px'
        }}
      ></div>
      
      {/* Simulated Roads */}
      <div className="absolute top-1/2 left-0 w-full h-8 bg-slate-300 -translate-y-1/2 rotate-2 opacity-50"></div>
      <div className="absolute top-0 left-1/3 w-8 h-full bg-slate-300 -rotate-3 opacity-50"></div>
      
      {/* Simulated Markers */}
      <div className="absolute inset-0 flex items-center justify-center">
        <div className="relative animate-bounce">
          <MapPin className="w-12 h-12 text-red-500 fill-red-200" />
          <div className="absolute top-12 left-1/2 -translate-x-1/2 w-4 h-1 bg-black/20 rounded-full blur-[2px]"></div>
        </div>
        
        {/* Info Box on Map */}
        <div className="absolute top-[calc(50%-80px)] left-1/2 -translate-x-1/2 bg-white px-4 py-2 rounded-lg shadow-xl border border-slate-200 whitespace-nowrap z-10">
          <p className="text-sm font-bold text-slate-800">{selectedData.name}</p>
          <p className="text-[10px] text-slate-500">위도: {selectedData.location.lat}, 경도: {selectedData.location.lng}</p>
        </div>
      </div>

      {/* Map UI Elements */}
      <div className="absolute bottom-6 right-6 flex flex-col gap-2">
        <button className="w-10 h-10 bg-white rounded-lg shadow-md flex items-center justify-center font-bold text-slate-600 hover:bg-slate-50 transition-colors">+</button>
        <button className="w-10 h-10 bg-white rounded-lg shadow-md flex items-center justify-center font-bold text-slate-600 hover:bg-slate-50 transition-colors">-</button>
      </div>
      
      <div className="absolute top-6 left-6 bg-white/90 backdrop-blur-sm px-3 py-1.5 rounded-full shadow-sm text-xs font-medium text-slate-700 flex items-center gap-2 border border-white/50">
        <div className="w-2 h-2 rounded-full bg-green-500"></div>
        상권 데이터 실시간 분석 중...
      </div>
    </div>
  );
}
