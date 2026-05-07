"use client";

import React from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Cell,
  PieChart,
  Pie,
} from "recharts";
import { EnglishAcademyStats } from "@/lib/data";
import { TrendingUp, Users, DollarSign, Award, ThumbsUp, ThumbsDown, BookOpen, Home } from "lucide-react";

interface StatsDashboardProps {
  data: EnglishAcademyStats;
}

export default function StatsDashboard({ data }: StatsDashboardProps) {
  const populationData = [
    { name: "유치원", value: data.schoolAgePopulation.kindergarten, color: "#60a5fa" },
    { name: "초등", value: data.schoolAgePopulation.elementary, color: "#34d399" },
    { name: "중등", value: data.schoolAgePopulation.middle, color: "#fbbf24" },
    { name: "고등", value: data.schoolAgePopulation.high, color: "#f87171" },
  ];

  const totalStudents = populationData.reduce((acc, curr) => acc + curr.value, 0);

  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
      {/* Summary Cards */}
      <div className="bg-white p-5 rounded-2xl border shadow-sm flex items-center gap-4">
        <div className="p-3 bg-blue-100 rounded-xl text-blue-600 shrink-0">
          <Users className="w-6 h-6" />
        </div>
        <div>
          <p className="text-xs text-gray-500 font-medium uppercase">학령 인구 합계</p>
          <p className="text-2xl font-bold">{totalStudents.toLocaleString()}명</p>
        </div>
      </div>

      <div className="bg-white p-5 rounded-2xl border shadow-sm flex items-center gap-4">
        <div className="p-3 bg-indigo-100 rounded-xl text-indigo-600 shrink-0">
          <Home className="w-6 h-6" />
        </div>
        <div>
          <p className="text-xs text-gray-500 font-medium uppercase">인근 거주 인원</p>
          <p className="text-2xl font-bold">{data.residentPopulation.toLocaleString()}명</p>
        </div>
      </div>

      <div className="bg-white p-5 rounded-2xl border shadow-sm flex items-center gap-4">
        <div className="p-3 bg-emerald-100 rounded-xl text-emerald-600 shrink-0">
          <BookOpen className="w-6 h-6" />
        </div>
        <div>
          <p className="text-xs text-gray-500 font-medium uppercase">영어학원 수</p>
          <p className="text-2xl font-bold">{data.englishAcademiesCount}개</p>
        </div>
      </div>

      {/* Population Chart */}
      <div className="bg-white p-6 rounded-2xl border shadow-sm lg:col-span-2">
        <h3 className="text-sm font-bold text-gray-800 mb-6 flex items-center gap-2">
          <Users className="w-4 h-4 text-blue-500" />
          학령 인구 구성 분석
        </h3>
        <div className="h-64 w-full">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={populationData}>
              <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
              <XAxis 
                dataKey="name" 
                axisLine={false} 
                tickLine={false} 
                tick={{ fill: '#64748b', fontSize: 12 }}
              />
              <YAxis 
                axisLine={false} 
                tickLine={false} 
                tick={{ fill: '#64748b', fontSize: 12 }}
              />
              <Tooltip 
                cursor={{ fill: '#f8fafc' }}
                contentStyle={{ borderRadius: '12px', border: 'none', boxShadow: '0 10px 15px -3px rgb(0 0 0 / 0.1)' }}
              />
              <Bar dataKey="value" radius={[6, 6, 0, 0]}>
                {populationData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={entry.color} />
                ))}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Market Score */}
      <div className="bg-white p-6 rounded-2xl border shadow-sm flex flex-col justify-center items-center">
        <h3 className="text-sm font-bold text-gray-800 mb-4 self-start flex items-center gap-2">
          <Award className="w-4 h-4 text-amber-500" />
          상권 종합 점수
        </h3>
        <div className="relative flex items-center justify-center">
          <ResponsiveContainer width={180} height={180}>
            <PieChart>
              <Pie
                data={[{ value: data.score }, { value: 100 - data.score }]}
                cx="50%"
                cy="50%"
                innerRadius={60}
                outerRadius={80}
                startAngle={90}
                endAngle={-270}
                paddingAngle={0}
                dataKey="value"
              >
                <Cell fill="#f59e0b" />
                <Cell fill="#f1f5f9" />
              </Pie>
            </PieChart>
          </ResponsiveContainer>
          <div className="absolute inset-0 flex flex-col items-center justify-center">
            <span className="text-4xl font-extrabold text-amber-500">{data.score}</span>
            <span className="text-[10px] text-gray-400 font-bold uppercase tracking-widest">Points</span>
          </div>
        </div>
        <p className="mt-4 text-xs text-gray-500 text-center leading-relaxed">
          {data.name}은(는) 상위 {Math.floor(100 - data.score)}%에 속하는<br />
          매우 유망한 상권입니다.
        </p>
      </div>

      {/* Top Academies */}
      <div className="bg-white p-6 rounded-2xl border shadow-sm lg:col-span-1">
        <h3 className="text-sm font-bold text-gray-800 mb-4 flex items-center gap-2">
          <TrendingUp className="w-4 h-4 text-indigo-500" />
          인근 주요 영어학원 (Top 10)
        </h3>
        <ul className="space-y-2 text-sm text-gray-600">
          {data.topAcademies.map((academy, idx) => (
            <li key={idx} className="flex items-center gap-3 bg-slate-50 px-3 py-2 rounded-lg">
              <span className="font-bold text-indigo-400 w-4">{idx + 1}</span>
              <span className="font-medium text-slate-700">{academy}</span>
            </li>
          ))}
        </ul>
      </div>

      {/* Pros & Cons */}
      <div className="bg-white p-6 rounded-2xl border shadow-sm lg:col-span-2 flex flex-col md:flex-row gap-6">
        <div className="flex-1">
          <h3 className="text-sm font-bold text-gray-800 mb-4 flex items-center gap-2">
            <ThumbsUp className="w-4 h-4 text-blue-500" />
            입지 장점
          </h3>
          <ul className="space-y-3">
            {data.pros.map((pro, idx) => (
              <li key={idx} className="flex items-start gap-2 text-sm text-gray-600">
                <div className="w-1.5 h-1.5 rounded-full bg-blue-400 mt-1.5 shrink-0"></div>
                <span className="leading-relaxed">{pro}</span>
              </li>
            ))}
          </ul>
        </div>
        <div className="w-px bg-slate-100 hidden md:block"></div>
        <div className="flex-1">
          <h3 className="text-sm font-bold text-gray-800 mb-4 flex items-center gap-2">
            <ThumbsDown className="w-4 h-4 text-red-500" />
            입지 단점 및 주의사항
          </h3>
          <ul className="space-y-3">
            {data.cons.map((con, idx) => (
              <li key={idx} className="flex items-start gap-2 text-sm text-gray-600">
                <div className="w-1.5 h-1.5 rounded-full bg-red-400 mt-1.5 shrink-0"></div>
                <span className="leading-relaxed">{con}</span>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}